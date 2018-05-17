# Yet Another Model Inference tool

![YAMI Logo](yami.png)

## About

Model inference has been developed over years for various purposes: reverse engineering
existing systems; automata learning in order to do predictions; application monitoring ; etc.
In particular it has been used to do regression testing on web-based applications. The
idea is to reverse engineer a usage model reflecting the current usages of the application
to test a new release of the application. This is particularly useful in web-based applications
since the navigation from link to link is not always obvious. For instance, a user may
directly access some resources using a direct URL, or the previous and forward buttons
in most of the web browsers allows navigation that are not reflected by the links of a
web page, etc. This is why usage models have gained in popularity in web-based application
testing and monitoring over the past few years.

### N-Gram Model Inference

There exists several techniques to infer a model: the Pautomac competition assess several
of them every year. Unfortunately, the considered models used in the competition do not
consider large inputs. As described by Verwer et al., the most efficient
way to treat real world models without loosing too much precision is the N-gram inference
technique. N-gram inference basically build a usage model (i.e., a Markov chain) from a set
of user sessions. A User session is a sequences of web pages, visited by the user. A n-gram
inference will build a usage model to predict when in a given state, which will be the
next state, based on the n-1 previous states. N-grams have been used with success to build
web based applications usage models in the past. As stated by Sprenkle et al., the growth
of the model will depend on the chosen 'n' but also on the size of the user sessions.
Indeed, if the n is high and the uses sessions small, the model will not growth further for
a higher value n+1. In our case, we choose to use, like Ghezzi et al., 2-gram model inference
in order to generate our usage model.

### Web Based Application Usage Model

Web-based application usage models are learned from user sessions. Those sessions are usually
recorded on the web server (e.g., in the Apache web log in our case) as a list of HTTP requests
received by the server. We choose to define a user session as a sequence of HTTP request coming
from the same IP address with a time-frame of x minutes after the last request. I.e., if the
same IP sends another request during the x minutes following its last request, we consider this
new request as part of the user session. During our generation process we choose a time-frame of 45 minutes.

To build the usage model, there are (mainly) 3 different ways to represent the HTTP requests which will
lead to 3 ways to define the transitions between states:

* Request Resource (RR): a user request in the user session will correspond to concatenation of the
request type (GET, POST, GET, or HEAD) and the requested resource. For instance, if a user requests
the main page of a web site, regardless of  the parameters he may provide for this HTTP request,
the entry in his user session will be `GET /index.php`.

* Request Resource parameters' Names (RRN): a user request in the user session will correspond to
the concatenation of the request type, the requested resource, and the parameters' names provided
for this request. For instance, `GET /index.php?course=`. The value of the parameters are ignored.

* Request Resource parameters' Names and Values (RRNV): in this last case, a user request in the
user session will correspond to the concatenation of the request type, the requested resource,
and the parameters' names and values. For instance, `GET /index.php?course=INFOB314`.

The size of the model will depend on the chosen request representation. The RRNV representation
may be too fine grained and the generated model may be too specific.


## Usage

Here is an example to infer a usage model from an Apache web log from the Main class:

```java
// Input Log file is the first parameter provided to the application
File input = new File(args[0]);

long startTime = System.currentTimeMillis();

// The bigram which will construct the model
final Bigram<ApacheUserRequest> bigram = new Bigram<>(
        UserRequesRRKeyGenerator.getInstance());

// The session builder (Apache sessions in this case)
ApacheUserSessionBuilder builder = ApacheUserSessionBuilder.newInstance()
        // Configure the log format to use
        .logFormat(ApacheLogFormatPatternBuilder.COMBINED_LOG_FORMAT);

// Add session listener that will enrich the model (via bigram) using the session
final List<Integer> sizes = Lists.newArrayList();
builder.addListener(new UserSessionProcessor<ApacheUserSession>() {
    int i = 0;
    @Override
    public void process(ApacheUserSession session) {
        sizes.add(session.size());
        i++;
        LOG.trace("Sessions processed: {}", i);
        LOG.trace("Session: {}", session);
        bigram.addTrace(session.iterator());
    }
});

// Include resources ending by '.php' or '/' or '.js'
builder.include((ApacheUserRequest request) -> {
    if (request == null) {
        LOG.error("Request is null!");
        return false;
    }
    return request.getResource() != null
            && (request.getResource().endsWith(".php")
            || request.getResource().endsWith("/") || request
                    .getResource().endsWith(".js"));
});

// Exclude localhost and jetpack requests
builder.exclude((ApacheUserRequest request) -> {
    if (request == null) {
        LOG.error("Request is null!");
        return true;
    }
    return request.getClient() == null
            || request.getClient().equals("localhost")
            || request.getClient().equals("127.0.0.1")
            || (request.getUserAgent() != null && request
                    .getUserAgent()
                    .equals("jetmon/1.0 (Jetpack Site Uptime Monitor by WordPress.com)"));
});

// Launch the session building from the input file
builder.buildSessions(new FileInputStream(input));

// Get the usage model from the Bigram
UsageModel model = bigram.getModel();

// Print XML model on System.out
Xml.print(model, System.out);
```

## References

This tool has been developped and used in the following publication:

```bibtex
@article{Devroey2017,
        author = {Devroey, Xavier and Perrouin, Gilles and Cordy, Maxime and Samih, Hamza and Legay, Axel and Schobbens, Pierre-Yves and Heymans, Patrick},
        title = {{Statistical prioritization for software product line testing: an experience report}},
        journal = {Software {\&} Systems Modeling},
        volume = {16},
        number = {1},
        month = {feb},
        pages = {153--171},
        issn = {1619-1366},
        publisher = {Springer},
        doi = {10.1007/s10270-015-0479-8},
        year = {2017}
}
```
