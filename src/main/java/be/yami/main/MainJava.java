package be.yami.main;

import be.vibes.dsl.io.Xml;
import be.vibes.ts.UsageModel;
import be.yami.exception.ModelGenerationException;
import be.yami.java.ClassMethodParametersKeyGenerator;
import be.yami.java.JsonMethodCallsSequenceBuilder;
import be.yami.java.MethodCall;
import be.yami.java.MethodCallSequence;
import be.yami.java.MultipleModelsProcessor;
import be.yami.ngram.Bigram;
import be.yami.ngram.NGram;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Main class provided to process Java method call sequences with
 * this library.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class MainJava {

    private static final Logger LOG = LoggerFactory.getLogger(MainJava.class);

    /**
     * Create a usage model from a Java method calls sequences JSON file.
     *
     * @param args Arguments from the command line. Input file name must be at
     * position 0. Usage models are generated in a folder with the same name as
     * the input file.
     * @throws java.lang.Exception Because shit happens...
     */
    public static void main(String[] args) throws Exception {

        Preconditions.checkArgument(args.length > 0, "Input file name must be provided as first argument! Usage models are generated in a folder with the same name as the input file.");

        // Input Log file is the first parameter provided to the application
        File input = new File(args[0]);

        long startTime = System.currentTimeMillis();

        // The bigram which will construct the model
        final MultipleModelsProcessor processor = new MultipleModelsProcessor() {
            @Override
            protected NGram<MethodCall> buildNewNGram(String name) {
                LOG.info("Bigram created for class {}", name);
                return new Bigram<>(name, ClassMethodParametersKeyGenerator.getInstance());
            }
        };

        // The session builder (Apache sessions in this case)
        JsonMethodCallsSequenceBuilder builder = JsonMethodCallsSequenceBuilder.newInstance();

        // Add session listener that will enrich the model (via bigram) using the session
        builder.addListener(processor);

        final List<Integer> sizes = Lists.newArrayList();
        builder.addListener((MethodCallSequence seq) -> {
            sizes.add(seq.size());
            LOG.info("Sequences processed: {}", sizes.size());
        });

        //builder.include(filter);
        //builder.exclude(filter);
        // Launch the session building from the input file 
        builder.buildSessions(new FileInputStream(input));

        // Get the usage models and print them
        File outFolder = new File(args[0].replace(".json", "").replace(".JSON", "").replace(".Json", ""));
        LOG.info("Printing models in folder {}", outFolder);
        if (!outFolder.exists()) {
            outFolder.mkdir();
        }
        processor.getNGrams().forEach((ngram) -> {
            try {
                LOG.info("Printing model {}", ngram.getName());
                UsageModel um = ngram.getModel();
                File output = new File(outFolder, ngram.getName() + ".xml");
                Xml.print(um, output);
            } catch (ModelGenerationException ex) {
                LOG.error("Exception while retrieving usage model for {}!", ngram.getName(), ex);
            }
        });

        // Print statistics
        double sum = 0.0;
        for (Integer i : sizes) {
            sum = sum + i;
        }
        double mean = sum / sizes.size();

        double temp = 0;
        for (Integer i : sizes) {
            temp = temp + (i - mean) * (i - mean);
        }
        double variance = temp / sizes.size();
        double stdev = Math.sqrt(variance);

        System.err.println("Sequences count = " + sizes.size());
        System.err.println("Average sequence size = " + mean);
        System.err.println("Stdev sequence size = " + stdev);
        System.err.println("Min sequence size = " + Collections.min(sizes));
        System.err.println("Max sequence size = " + Collections.max(sizes));
        System.err.println("Computation time = " + (System.currentTimeMillis() - startTime) / 1000 + " sec.");
    }

}
