package com.vaadin.starter.bakery.gatling;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Barista extends Simulation {

  // The URL of the system under test
  String baseUrl = System.getProperty("gatling.baseUrl", "http://localhost:8080");

  // The total number of simulated user sessions
  // NOTE: the number of concurrent sessions is lower because sessions start one by one
  // with a given interval and some may finish before the last session starts.
  int sessionCount = Integer.parseInt(System.getProperty("gatling.sessionCount", "100"));

  // The interval (in milliseconds) between starting new user sessions
  int sessionStartInterval = Integer.parseInt(System.getProperty("gatling.sessionStartInterval", "100"));

  // The repeat count of the scenario, by default executed only once
  int sessionRepeats = Integer.parseInt(System.getProperty("gatling.sessionRepeats", "1"));

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl(baseUrl)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/20100101 Firefox/105.0");
  
  private Map<CharSequence, String> headers_0 = Map.ofEntries(
    Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"),
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Upgrade-Insecure-Requests", "1")
  );
  
  private Map<CharSequence, String> headers_1 = Map.ofEntries(
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Pragma", "no-cache")
  );
  
  private Map<CharSequence, String> headers_3 = Map.ofEntries(
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Content-type", "application/json; charset=UTF-8"),
    Map.entry("Origin", "http://localhost:8080"),
    Map.entry("Pragma", "no-cache")
  );
  
  private Map<CharSequence, String> headers_6 = Map.ofEntries(
    Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"),
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Origin", "http://localhost:8080"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Upgrade-Insecure-Requests", "1")
  );

  private ScenarioBuilder scn = scenario("Barista").repeat(sessionRepeats).on(
    exec(
      http("request_0")
        .get("/")
        .headers(headers_0)
    )
    .pause(Duration.ofMillis(147))
    .exec(
      http("request_1")
        .get("/?v-r=init&location=login")
        .headers(headers_1)
        .check(regex("v-uiId\":(\\d+)").saveAs("uiId"))
        .check(regex("Vaadin-Security-Key\":\\s?\"([^\"]*)").saveAs("seckey"))
    )
    .exec(
      http("request_3")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0003_request.json"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"tag\",+\"feat\":[0-9]*,+\"value\":\"vaadin-login-overlay").saveAs("loginOverlay"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_4")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0004_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(15)
    .exec(
      http("request_5")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0005_request.json"))
    )
    .exec(
      http("request_6")
        .post("/login")
        .headers(headers_6)
        .formParam("_csrf", "#{seckey}")
        .formParam("username", "barista@vaadin.com")
        .formParam("password", "barista")
    )
    .exec(
      http("request_7")
        .get("/?v-r=init&location=")
        .headers(headers_1)
        .check(regex("Vaadin-Security-Key\":\\s?\"([^\"]*)").saveAs("seckey"))
        .check(regex("v-uiId\":(\\d+)").saveAs("uiId"))
    )
    .pause(Duration.ofMillis(443))
    .exec(
      http("request_9")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0009_request.json"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"tag\",+\"feat\":[0-9]*,+\"value\":\"vaadin-app-layout\"").saveAs("appLoId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"tag\",+\"feat\":[0-9]*,+\"value\":\"vaadin-tabs\"").saveAs("tabsId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"tag\",+\"feat\":[0-9]*,+\"value\":\"vaadin-confirm-dialog\"").saveAs("confirmId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"grid\"").saveAs("gridId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"field").saveAs("searchId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"action\"").saveAs("newButtonId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"dialog\"").saveAs("dialogId"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_10")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0010_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(Duration.ofMillis(172))
    .exec(
      http("request_11")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0011_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(Duration.ofMillis(244))
    .exec(
      http("request_12")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0012_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_13")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0013_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(Duration.ofMillis(166))
    .exec(
      http("request_14")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0014_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_15")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0015_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(2)
    .exec(
      http("request_16")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0016_request.json"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"status\"").saveAs("statusId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"dueTime\"").saveAs("dueTimeId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"dueDate\"").saveAs("dueDateId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"pickupLocation\"").saveAs("storeId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"customerName\"").saveAs("customerId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"customerNumber\"").saveAs("phoneId"))
        .check(regex("node\":(\\d+),\"type\":\"put\",\"key\":\"payload\",\"feat\":[0-9]*,\"value\":\\{\"type\":\"@id\",\"payload\":\"products\"").saveAs("productsId"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"review\"").saveAs("reviewId"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_17")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0017_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(Duration.ofMillis(123))
    .exec(
      http("request_18")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0018_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(2)
    .exec(
      http("request_19")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0019_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_20")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0020_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(Duration.ofMillis(713))
    .exec(
      http("request_21")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0021_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(4)
    .exec(
      http("request_22")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0022_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(3)
    .exec(
      http("request_23")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0023_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_24")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0024_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(Duration.ofMillis(723))
    .exec(
      http("request_25")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0025_request.json"))
        .check(regex("node\":(\\d+),\"type\":\"put\",\"key\":\"selectedItem\",\"feat\":[0-9],\"value\":\\{\"key\":\"[0-9]\",\"label\":\"Strawberry Bun").saveAs("products1Id2"))
        .check(regex("payload\":\"products.{3000,5000}node\":(\\d+),\"type\":\"put\",\"key\":\"payload\",\"feat\":[0-9]*,\"value\":\\{\"type\":\"@id\",\"payload\":\"products\"").saveAs("products2Id"))
        .check(regex("node\":(\\d+),\"type\":\"put\",\"key\":\"payload\",\"feat\":[0-9]*,\"value\":\\{\"type\":\"@id\",\"payload\":\"amount\"").saveAs("amountId"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_26")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0026_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(1)
    .exec(
      http("request_27")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0027_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(1)
    .exec(
      http("request_28")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0028_request.json"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"payload\",+\"feat\":[0-9]*,+\"value\":\\{+\"type\":\"@id\",+\"payload\":\"save\"").saveAs("saveId"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(1)
    .exec(
      http("request_29")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0029_request.json"))
        .check(regex("node\":(\\d+),+\"type\":\"put\",+\"key\":\"tag\",+\"feat\":[0-9]*,+\"value\":\"vaadin-notification\"").saveAs("notificationId"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .exec(
      http("request_30")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0030_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId"))
        .check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    )
    .pause(Duration.ofMillis(312))
    .exec(
      http("request_31")
        .post("/?v-r=uidl&v-uiId=#{uiId}")
        .headers(headers_3)
        .body(ElFileBody("barista/0031_request.json"))
        .check(regex("syncId\":([0-9]*)").saveAs("syncId")).check(regex("clientId\":([0-9]*)").saveAs("clientId"))
    ))
    .exec(flushHttpCache())
    .exec(flushSessionCookies());
  {
	  setUp(scn.injectOpen(rampUsers(sessionCount).during(sessionStartInterval))).protocols(httpProtocol);
  }
}
