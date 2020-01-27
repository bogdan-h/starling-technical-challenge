# starling-technical-challenge
A “round-up” feature for Starling customers using the public developer API that is available to all customers and partners.

# Build & Run

`gradle clean build` will run all tests (unit and acceptance) and generate an executable JAR under `build/libs/roundup-0.1.0.jar`.

You can run the project via `java -jar build/libs/roundup-0.1.0.jar` and supply the environment variables in the command. Or you can run or debug the `Application` class from IntelliJ and supply the environment variables in the Intellij run configuration.

You can also run and debug all tests (both unit and acceptance) through IntelliJ. Acceptance tests run via `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)` annotation so debugging is pretty native in IntelliJ.

# Design

I opted for a REST resource that can be invoked to trigger the round-up. I went for this option as it is more likely that such a feature would be used in this way in reality. I tried to design the resource in line with the current Starling API standards that I found in your Swagger docs at [Starling API Docs](https://developer.starlingbank.com/docs).

Also, I opted to satisfy the `take all the transactions in a given week` requirement by implementing the resource with `minTransactionTimestamp` and `maxTransactionTimestamp` query parameters, such that the resource is similar to the existing `GET /api/v2/feed/account/{accountUid}/category/{categoryUid}/transactions-between` resource. These query parameters are simply passed through to the Feed resource.

Another deliberate choice is that the resource expects a savings goal (identified by the `{savingsGoalUid}` path param) to already exist. In my testing I created a savings goal prior to executing the resource created in this exercise.

I opted for Spring 4 as this is what I am most comfortable with currently in Java; this is the technology I supported mostly in production. I played a bit recently with Spring Reactor though in a personal project and it looks pretty good.

# Testing

Throughout my career I worked both in teams focusing on TDD via unit tests as well as teams focusing on BDD via acceptance tests. I, personally, see value in both and think they complement each other. I tend to have a lot more confidence in acceptance tests, but find unit tests very relevant for hotspots of business logic in the code. That being said, I am adaptable and understand that every company/team evolves differently and I am happy to work in various workflows.

Therefore, I began the project in a BDD fashion and wrote acceptance tests (as the beginning of the project was mostly Spring boilerplate code on the controller and HTTP clients). These acceptance tests treat the entire system as a black box and interact with the system only at its edges (calling and asserting the REST resource, stubbing the HTTP clients via WireMock).

Then as I started introducing business logic I introduced unit tests as it is more readable (unit test classes correspond to main test classes) and more concise (testing lots of input cases in a more streamlined way).

I have also retrospectively added `AccountsClientTest`, `TransactionFeedClientTest`, `SavingsGoalClientTest` and `AccountsControllerTest` to prove techniques of writing unit tests for controllers and HTTP clients. These are duplicating acceptance tests but I added them just to prove the point. Actually, with these particular two test classes; fundamentally I want them to be unit tests but they can be considered integration tests since the `@RestClientTest(AccountsClient.class)` and `@SpringBootTest` annotations wire a bit more Spring under the hood.

Finally, I performed a manual happy path test on a standalone server against the Starling sandbox environment. (And found out that currencies are mandatory so this test was quite valuable in fact).

# Timeline

The project took me roughly 6-8 hours. I know the guidelines mentioned 2-3 hours, but I really wanted to get the job done well and submit something I am happy about.  Up to 3 hours of this time however has been spent on initial project boilerplate code and additional testing to prove techniques (`AccountsClientTest`, `TransactionFeedClientTest`, `SavingsGoalClientTest` and `AccountsControllerTest`).

In reality, I would probably balance speed vs quality in order to meet product deadlines, or negotiate the deadlines if there is good reason to spend more time on the code.

# Further Improvements

One big feature to finish off the project correctly is to handle currencies. At the moment, I simply pick the currency of the first feed item and use that as the currency for the roundup amount.
A more correct way will require at least two additional HTTP calls:
- to get the exchange rates
- to get the savings goal in order to obtain the currency into which roundup amounts from feed items should be calculated into

I would also use Java Currency API instead of hardcoding the currency as a String (e.g. for performing validation of the currency).

I have not implemented this as it would have required considerable more time and it would have mostly been similar work to the work that has been submitted. A performance improvement that could be done is to make 3 concurrent HTTP calls (to accounts, savings goal and exchange rates) as the calls are not dependent on each other.

Since I created a REST resource, the project needs Swagger docs. I would have also implemented this if I had more time, potentially using OpenAPI autogeneration.  

The HTTP clients can be improved via more elaborate fault-tolerance libraries which are becoming more important in a micro-services platform (e.g. [resilience4j](https://github.com/resilience4j/resilience4j), [sentinel](https://github.com/alibaba/Sentinel))