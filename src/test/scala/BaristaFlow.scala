import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BaristaFlow extends Simulation {

  val baseURL = "http://localhost:8080"

  // how many percent of html request are served from simulated "cache", rest are loaded normally
  // e.g. cacheRate = 70d: 70% of virtual users uses virtual cache thus html files are not loaded for them
  val cacheRate = 70d

  val httpProtocol = http
    .baseURL(baseURL)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:55.0) Gecko/20100101 Firefox/55.0")

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Upgrade-Insecure-Requests" -> "1")
  val headers_98 = Map("Pragma" -> "no-cache")
  val headers_99 = Map("Content-type" -> "application/json; charset=UTF-8")

  val initSyncAndClientIds = exec((session) => {
    session.setAll(
      "syncId" -> 0,
      "clientId" -> 0
    )
  })

  val url = "/"
  val uidlUrl = url + "?v-r=uidl&v-uiId=${uiId}"

  val uIdExtract = regex(""""v-uiId":\s(\d+)""").saveAs("uiId")
  val ordersIdExtract = regex("""node":\s(\d+),\n\s*"type":\s"put",\n\s*"key":\s"orders",""").saveAs("ordersId")
  val bakeryAppIdExtract = regex("""node":\s(\d+),\n\s*"type":\s"put",\n\s*"key":\s"tag",\n\s*"feat":\s0,\n\s*"value":\s"bakery-app""").saveAs("bakeryAppId")
  val newOrderButtonIdExtract = regex("""node":\s(\d+),\n\s*"type":\s"put",\n\s*"key":\s"buttonText",\n\s*"feat":\s1,\n\s*"value":\s"New\sorder""").saveAs("newOrderButtonId")
  val newOrderButton2IdExtract = regex("""node":\s(\d+),\n\s*"type":\s"put",\n\s*"key":\s"id",\n\s*"feat":\s3,\n\s*"value":\s"action""").saveAs("newOrderButton2Id")
  val navigationIdExtract = regex("""node":\s(\d+),\n\s*"type":\s"put",\n\s*"key":\s"id",\n\s*"feat":\s3,\n\s*"value":\s"navigation""").saveAs("navigationId")
  val ordersPageIdExtract = regex("""node":(\d+),"type":"splice","feat":22,"index":0,"add":\["loadOrdersPage""").saveAs("ordersPageId")
  val textfield1IdExtract = regex("""node":\s(\d+),\n\s*"type":\s"put",\n\s*"key":\s"placeholder",\n\s*"feat":\s1,\n\s*"value":\s"Search""").saveAs("textfield1Id")
  val clearButtonIdExtract = regex("""node":\s(\d+),\n\s*"type":\s"put",\n\s*"key":\s"id",\n\s*"feat":\s3,\n\s*"value":\s"clear""").saveAs("clearButtonId")

  val syncIdExtract = regex("""syncId":\s?([0-9]*),""").saveAs("syncId")
  val clientIdExtract = regex("""clientId":\s?([0-9]*),""").saveAs("clientId")
  val xsrfTokenExtract = regex("""Vaadin-Security-Key":\s"([^"]*)""").saveAs("seckey")

  val clickNewOrderResponse = regex("""_openNewOrderDialog""")
  val submitOrderFormResponse = regex("""Order was not saved""")
  val navigateToDashboardResponse = regex("""bakery-dashboard.html""")
  val loadOrdersPage1Response = regex("""pageNumber\\\":0""")
  val loadOrdersPage2Response = regex("""pageNumber\\\":1""")
  val navigateToStorefrontResponse = regex("""put","key":"page","feat":1,"value":"storefront""")

  val init = exec(http("Load app")
    .get(url)
    .headers(headers_0)
    .check(uIdExtract)
    .check(xsrfTokenExtract)

  ).exec(initSyncAndClientIds)

  val attachNavigation =
    exec(http("Initial request")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_1_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
    )

  val performLogin =
    exec(http("Perform login")
      .post("/login")
      .headers(headers_0)
      .formParam("username", "barista@vaadin.com")
      .formParam("password", "barista")
      .check(uIdExtract)
      .check(ordersIdExtract)
      .check(bakeryAppIdExtract)
      .check(newOrderButtonIdExtract)
      .check(newOrderButton2IdExtract)
      .check(textfield1IdExtract)
      .check(clearButtonIdExtract)
      .check(navigationIdExtract)
      .check(syncIdExtract)
      .check(clientIdExtract)
    )

  val attachSearchAndNavigation =
    exec(http("attachSearchAndNavigation")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_2_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
    )

  val attachButtons =
    exec(http("attachButtons")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_3_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
    )

  val clickNewOrder =
    exec(http("clickNewOrder")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_4_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
      .check(clickNewOrderResponse)
    )

  val submitOrderForm =
    exec(http("Submit order form")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_5_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
      .check(submitOrderFormResponse))

  val navigateToDashboard =
    exec(http("Navigate to Dashboard")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_6_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
      .check(ordersPageIdExtract)
      .check(navigateToDashboardResponse))

  val loadOrdersPage1 =
    exec(http("loadOrdersPage1")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_7_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
      .check(loadOrdersPage1Response))

  val loadOrdersPage2 =
    exec(http("loadOrdersPage2")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_8_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
      .check(loadOrdersPage2Response))

  val navigateToStorefront =
    exec(http("Navigate to Storefront")
      .post(uidlUrl)
      .headers(headers_99)
      .body(ElFileBody("BaristaFlow_9_request.txt"))
      .check(syncIdExtract)
      .check(clientIdExtract)
      .check(navigateToStorefrontResponse))


  val initialHtmlFiles1 =
    exec(http("Load initial html files 1/4")
    .get("/src/app/bakery-app.html")
    .headers(headers_0)
    .resources(
      http("request_3")
        .get("/src/login/bakery-login.html"),
      http("request_4")
        .get("/src/app/bakery-navigation.html"),
      http("request_5")
        .get("/bower_components/polymer/polymer-element.html"),
      http("request_6")
        .get("/src/app/style-modules/shared-styles.html"),
      http("request_7")
        .get("/bower_components/iron-icon/iron-icon.html"),
      http("request_8")
        .get("/bower_components/iron-media-query/iron-media-query.html"),
      http("request_9")
        .get("/bower_components/paper-badge/paper-badge.html"),
      http("request_10")
        .get("/bower_components/paper-tabs/paper-tabs.html"),
      http("request_11")
        .get("/bower_components/paper-tabs/paper-tab.html"),
      http("request_12")
        .get("/src/elements/user-avatar.html"),
      http("request_13")
        .get("/src/app/user-popup-menu.html"),
      http("request_14")
        .get("/bower_components/polymer/lib/mixins/element-mixin.html"),
      http("request_15")
        .get("/bower_components/vaadin-themes/valo/typography.html"),
      http("request_16")
        .get("/bower_components/vaadin-themes/valo/color.html"),
      http("request_17")
        .get("/bower_components/vaadin-themes/valo/style.html"),
      http("request_18")
        .get("/bower_components/vaadin-themes/valo/sizing-and-spacing.html"),
      http("request_19")
        .get("/bower_components/vaadin-themes/valo/vaadin-button.html"),
      http("request_20")
        .get("/bower_components/vaadin-themes/valo/vaadin-checkbox.html"),
      http("request_21")
        .get("/bower_components/vaadin-themes/valo/icons.html"),
      http("request_22")
        .get("/bower_components/vaadin-themes/valo/vaadin-form-item.html"),
      http("request_23")
        .get("/bower_components/vaadin-themes/valo/vaadin-text-field.html")))

  val initialHtmlFiles2 =
    exec(http("Load initial html files 2/4")
      .get("/bower_components/vaadin-themes/valo/vaadin-combo-box.html")
      .headers(headers_0)
      .resources(
      http("request_25")
        .get("/bower_components/polymer/polymer.html"),
      http("request_26")
        .get("/bower_components/vaadin-themes/valo/vaadin-form-layout.html"),
      http("request_27")
        .get("/bower_components/vaadin-themes/valo/vaadin-date-picker.html"),
      http("request_28")
        .get("/bower_components/iron-meta/iron-meta.html"),
      http("request_29")
        .get("/bower_components/iron-flex-layout/iron-flex-layout.html"),
      http("request_30")
        .get("/bower_components/iron-resizable-behavior/iron-resizable-behavior.html"),
      http("request_31")
        .get("/bower_components/paper-styles/default-theme.html"),
      http("request_32")
        .get("/bower_components/paper-styles/typography.html"),
      http("request_33")
        .get("/bower_components/iron-menu-behavior/iron-menubar-behavior.html"),
      http("request_34")
        .get("/bower_components/paper-icon-button/paper-icon-button.html"),
      http("request_35")
        .get("/bower_components/paper-styles/color.html"),
      http("request_36")
        .get("/bower_components/paper-tabs/paper-tabs-icons.html"),
      http("request_37")
        .get("/bower_components/iron-behaviors/iron-button-state.html"),
      http("request_38")
        .get("/bower_components/iron-behaviors/iron-control-state.html"),
      http("request_39")
        .get("/bower_components/paper-behaviors/paper-ripple-behavior.html"),
      http("request_40")
        .get("/src/elements/dialog-mixins.html"),
      http("request_41")
        .get("/bower_components/polymer/lib/utils/boot.html"),
      http("request_42")
        .get("/bower_components/polymer/lib/utils/settings.html"),
      http("request_43")
        .get("/bower_components/polymer/lib/utils/mixin.html"),
      http("request_44")
        .get("/bower_components/polymer/lib/utils/case-map.html"),
      http("request_45")
        .get("/bower_components/polymer/lib/utils/style-gather.html")))

  val initialHtmlFiles3 =
    exec(http("Load initial html files 3/4")
      .get("/bower_components/polymer/lib/utils/resolve-url.html")
      .headers(headers_0)
      .resources(http("request_47")
        .get("/bower_components/polymer/lib/elements/dom-module.html"),
      http("request_48")
        .get("/bower_components/vaadin-valo-theme/typography.html"),
      http("request_49")
        .get("/bower_components/vaadin-valo-theme/color.html"),
      http("request_50")
        .get("/bower_components/polymer/lib/mixins/property-effects.html"),
      http("request_51")
        .get("/bower_components/vaadin-valo-theme/style.html"),
      http("request_52")
        .get("/bower_components/vaadin-valo-theme/sizing-and-spacing.html"),
      http("request_53")
        .get("/bower_components/vaadin-valo-theme/vaadin-checkbox.html"),
      http("request_54")
        .get("/bower_components/vaadin-valo-theme/vaadin-button.html"),
      http("request_55")
        .get("/bower_components/vaadin-valo-theme/icons.html"),
      http("request_56")
        .get("/bower_components/vaadin-valo-theme/vaadin-text-field.html"),
      http("request_57")
        .get("/bower_components/vaadin-valo-theme/vaadin-form-item.html"),
      http("request_58")
        .get("/bower_components/vaadin-valo-theme/vaadin-combo-box.html"),
      http("request_59")
        .get("/bower_components/polymer/lib/legacy/polymer-fn.html"),
      http("request_60")
        .get("/bower_components/polymer/lib/legacy/legacy-element-mixin.html"),
      http("request_61")
        .get("/bower_components/polymer/lib/legacy/templatizer-behavior.html"),
      http("request_62")
        .get("/bower_components/polymer/lib/elements/dom-bind.html"),
      http("request_63")
        .get("/bower_components/polymer/lib/elements/dom-repeat.html"),
      http("request_64")
        .get("/bower_components/polymer/lib/elements/dom-if.html"),
      http("request_65")
        .get("/bower_components/polymer/lib/elements/custom-style.html"),
      http("request_66")
        .get("/bower_components/polymer/lib/elements/array-selector.html"),
      http("request_67")
        .get("/bower_components/vaadin-valo-theme/vaadin-form-layout.html"),
      http("request_68")
        .get("/bower_components/polymer/lib/legacy/mutable-data-behavior.html"),
      http("request_69")
        .get("/bower_components/vaadin-valo-theme/vaadin-date-picker.html"),
      http("request_70")
        .get("/bower_components/font-roboto/roboto.html"),
      http("request_71")
        .get("/bower_components/iron-menu-behavior/iron-menu-behavior.html"),
      http("request_72")
        .get("/bower_components/paper-behaviors/paper-inky-focus-behavior.html"),
      http("request_73")
        .get("/bower_components/iron-iconset-svg/iron-iconset-svg.html")))

  val initialHtmlFiles4 =
    exec(http("Load initial html files 4/4")
      .get("/bower_components/iron-a11y-keys-behavior/iron-a11y-keys-behavior.html")
      .headers(headers_0)
      .resources(
      http("request_75")
        .get("/bower_components/paper-ripple/paper-ripple.html"),
      http("request_76")
        .get("/bower_components/polymer/lib/utils/path.html"),
      http("request_77")
        .get("/bower_components/polymer/lib/mixins/property-accessors.html"),
      http("request_78")
        .get("/bower_components/polymer/lib/mixins/template-stamp.html"),
      http("request_79")
        .get("/bower_components/polymer/lib/legacy/class.html"),
      http("request_80")
        .get("/bower_components/polymer/lib/utils/templatize.html"),
      http("request_81")
        .get("/bower_components/shadycss/apply-shim.html"),
      http("request_82")
        .get("/bower_components/polymer/lib/mixins/gesture-event-listeners.html"),
      http("request_83")
        .get("/bower_components/polymer/lib/utils/import-href.html"),
      http("request_84")
        .get("/bower_components/polymer/lib/utils/render-status.html"),
      http("request_85")
        .get("/bower_components/polymer/lib/utils/unresolved.html"),
      http("request_86")
        .get("/bower_components/polymer/lib/legacy/polymer.dom.html"),
      http("request_87")
        .get("/bower_components/polymer/lib/mixins/mutable-data.html"),
      http("request_88")
        .get("/bower_components/polymer/lib/utils/debounce.html"),
      http("request_89")
        .get("/bower_components/polymer/lib/utils/flush.html"),
      http("request_90")
        .get("/bower_components/shadycss/custom-style-interface.html"),
      http("request_91")
        .get("/bower_components/polymer/lib/utils/array-splice.html"),
      http("request_92")
        .get("/bower_components/vaadin-valo-theme/font-icons.html"),
      http("request_93")
        .get("/bower_components/iron-selector/iron-multi-selectable.html"),
      http("request_94")
        .get("/bower_components/polymer/lib/utils/async.html"),
      http("request_95")
        .get("/bower_components/polymer/lib/utils/gestures.html"),
      http("request_96")
        .get("/bower_components/polymer/lib/utils/flattened-nodes-observer.html"),
      http("request_97")
        .get("/bower_components/iron-selector/iron-selectable.html"),
      http("request_98")
        .get("/bower_components/iron-selector/iron-selection.html")))

  val afterLoginHtmlFiles1 =
    exec(http("Load html files after login (1/8)")
    .get("/src/app/bakery-app.html")
    .headers(headers_0)
    .resources(http("request_104")
      .get("/src/storefront/bakery-storefront.html"),
      http("request_105")
        .get("/src/app/bakery-navigation.html"),
      http("request_106")
        .get("/bower_components/polymer/polymer-element.html"),
      http("request_107")
        .get("/src/app/style-modules/shared-styles.html"),
      http("request_108")
        .get("/bower_components/polymer/lib/utils/debounce.html"),
      http("request_109")
        .get("/bower_components/polymer/lib/utils/flush.html"),
      http("request_110")
        .get("/bower_components/iron-media-query/iron-media-query.html"),
      http("request_111")
        .get("/bower_components/vaadin-grid/vaadin-grid.html"),
      http("request_112")
        .get("/src/app/bakery-search.html"),
      http("request_113")
        .get("/src/storefront/storefront-item-detail-wrapper.html"),
      http("request_114")
        .get("/src/storefront/storefront-item-edit-wrapper.html"),
      http("request_115")
        .get("/src/elements/utils-mixin.html"),
      http("request_116")
        .get("/bower_components/iron-icon/iron-icon.html"),
      http("request_117")
        .get("/bower_components/paper-badge/paper-badge.html"),
      http("request_118")
        .get("/bower_components/paper-tabs/paper-tabs.html"),
      http("request_119")
        .get("/src/app/user-popup-menu.html"),
      http("request_120")
        .get("/src/elements/user-avatar.html"),
      http("request_121")
        .get("/bower_components/paper-tabs/paper-tab.html"),
      http("request_122")
        .get("/bower_components/polymer/lib/mixins/element-mixin.html"),
      http("request_123")
        .get("/bower_components/vaadin-themes/valo/typography.html"),
      http("request_124")
        .get("/bower_components/vaadin-themes/valo/color.html")))

  val afterLoginHtmlFiles2 =
    exec(http("Load html files after login (2/8)")
      .get("/bower_components/vaadin-themes/valo/sizing-and-spacing.html")
      .headers(headers_0)
      .resources(
      http("request_126")
        .get("/bower_components/vaadin-themes/valo/style.html"),
      http("request_127")
        .get("/bower_components/vaadin-themes/valo/icons.html"),
      http("request_128")
        .get("/bower_components/vaadin-themes/valo/vaadin-button.html"),
      http("request_129")
        .get("/bower_components/vaadin-themes/valo/vaadin-text-field.html"),
      http("request_130")
        .get("/bower_components/vaadin-themes/valo/vaadin-checkbox.html"),
      http("request_131")
        .get("/bower_components/vaadin-themes/valo/vaadin-date-picker.html"),
      http("request_132")
        .get("/bower_components/vaadin-themes/valo/vaadin-form-layout.html"),
      http("request_133")
        .get("/bower_components/vaadin-themes/valo/vaadin-form-item.html"),
      http("request_134")
        .get("/bower_components/vaadin-themes/valo/vaadin-combo-box.html"),
      http("request_135")
        .get("/bower_components/polymer/lib/utils/boot.html"),
      http("request_136")
        .get("/bower_components/polymer/lib/utils/mixin.html"),
      http("request_137")
        .get("/bower_components/polymer/lib/utils/async.html"),
      http("request_138")
        .get("/bower_components/polymer/polymer.html"),
      http("request_139")
        .get("/bower_components/iron-resizable-behavior/iron-resizable-behavior.html"),
      http("request_140")
        .get("/bower_components/vaadin-grid/vaadin-grid-column.html"),
      http("request_141")
        .get("/bower_components/vaadin-grid/vaadin-grid-table.html"),
      http("request_142")
        .get("/bower_components/vaadin-grid/vaadin-grid-active-item-behavior.html"),
      http("request_143")
        .get("/bower_components/vaadin-grid/vaadin-grid-row-details-behavior.html"),
      http("request_144")
        .get("/bower_components/vaadin-grid/vaadin-grid-data-provider-behavior.html"),
      http("request_145")
        .get("/bower_components/vaadin-grid/vaadin-grid-dynamic-columns-behavior.html"),
      http("request_146")
        .get("/bower_components/vaadin-grid/vaadin-grid-selection-behavior.html"),
      http("request_147")
        .get("/bower_components/vaadin-grid/vaadin-grid-sort-behavior.html"),
      http("request_148")
        .get("/bower_components/vaadin-grid/vaadin-grid-filter-behavior.html"),
      http("request_149")
        .get("/bower_components/vaadin-grid/vaadin-grid-column-reordering-behavior.html"),
      http("request_150")
        .get("/bower_components/vaadin-grid/vaadin-grid-array-data-provider-behavior.html"),
      http("request_151")
        .get("/bower_components/vaadin-checkbox/vaadin-checkbox.html"),
      http("request_152")
        .get("/bower_components/vaadin-text-field/vaadin-text-field.html")))

  val afterLoginHtmlFiles3 =
    exec(http("Load html files after login (3/8)")
      .get("/src/app/bakery-icons.html")
      .headers(headers_0)
      .resources(http("request_154")
        .get("/src/storefront/storefront-item.html"),
      http("request_155")
        .get("/src/elements/item-detail-inline.html"),
      http("request_156")
        .get("/src/storefront/storefront-item-detail.html"),
      http("request_157")
        .get("/src/elements/item-detail-dialog.html"),
      http("request_158")
        .get("/src/storefront/storefront-item-edit.html"),
      http("request_159")
        .get("/bower_components/paper-toast/paper-toast.html"),
      http("request_160")
        .get("/src/elements/confirm-dialog.html"),
      http("request_161")
        .get("/bower_components/iron-meta/iron-meta.html"),
      http("request_162")
        .get("/bower_components/iron-flex-layout/iron-flex-layout.html"),
      http("request_163")
        .get("/bower_components/paper-styles/default-theme.html"),
      http("request_164")
        .get("/bower_components/paper-styles/typography.html"),
      http("request_165")
        .get("/bower_components/iron-menu-behavior/iron-menubar-behavior.html"),
      http("request_166")
        .get("/bower_components/paper-icon-button/paper-icon-button.html"),
      http("request_167")
        .get("/bower_components/paper-styles/color.html"),
      http("request_168")
        .get("/bower_components/paper-tabs/paper-tabs-icons.html"),
      http("request_169")
        .get("/src/elements/dialog-mixins.html"),
      http("request_170")
        .get("/bower_components/iron-behaviors/iron-button-state.html"),
      http("request_171")
        .get("/bower_components/iron-behaviors/iron-control-state.html"),
      http("request_172")
        .get("/bower_components/paper-behaviors/paper-ripple-behavior.html"),
      http("request_173")
        .get("/bower_components/polymer/lib/utils/settings.html"),
      http("request_174")
        .get("/bower_components/polymer/lib/utils/case-map.html"),
      http("request_175")
        .get("/bower_components/polymer/lib/utils/style-gather.html")))

  val afterLoginHtmlFiles4 =
    exec(http("Load html files after login (4/8)")
      .get("/bower_components/polymer/lib/utils/resolve-url.html")
      .headers(headers_0)
      .resources(
      http("request_177")
        .get("/bower_components/polymer/lib/elements/dom-module.html"),
      http("request_178")
        .get("/bower_components/vaadin-valo-theme/typography.html"),
      http("request_179")
        .get("/bower_components/polymer/lib/mixins/property-effects.html"),
      http("request_180")
        .get("/bower_components/vaadin-valo-theme/color.html"),
      http("request_181")
        .get("/bower_components/vaadin-valo-theme/sizing-and-spacing.html"),
      http("request_182")
        .get("/bower_components/vaadin-valo-theme/style.html"),
      http("request_183")
        .get("/bower_components/vaadin-valo-theme/icons.html"),
      http("request_184")
        .get("/bower_components/vaadin-valo-theme/vaadin-button.html"),
      http("request_185")
        .get("/bower_components/vaadin-valo-theme/vaadin-text-field.html"),
      http("request_186")
        .get("/bower_components/vaadin-valo-theme/vaadin-checkbox.html"),
      http("request_187")
        .get("/bower_components/vaadin-valo-theme/vaadin-date-picker.html"),
      http("request_188")
        .get("/bower_components/vaadin-valo-theme/vaadin-form-item.html"),
      http("request_189")
        .get("/bower_components/vaadin-valo-theme/vaadin-form-layout.html"),
      http("request_190")
        .get("/bower_components/vaadin-valo-theme/vaadin-combo-box.html"),
      http("request_191")
        .get("/bower_components/polymer/lib/legacy/polymer-fn.html"),
      http("request_192")
        .get("/bower_components/polymer/lib/legacy/legacy-element-mixin.html"),
      http("request_193")
        .get("/bower_components/polymer/lib/legacy/templatizer-behavior.html"),
      http("request_194")
        .get("/bower_components/polymer/lib/elements/dom-bind.html"),
      http("request_195")
        .get("/bower_components/polymer/lib/elements/dom-repeat.html"),
      http("request_196")
        .get("/bower_components/polymer/lib/elements/dom-if.html"),
      http("request_197")
        .get("/bower_components/polymer/lib/elements/array-selector.html"),
      http("request_198")
        .get("/bower_components/polymer/lib/elements/custom-style.html"),
      http("request_199")
        .get("/bower_components/polymer/lib/legacy/mutable-data-behavior.html"),
      http("request_200")
        .get("/bower_components/vaadin-grid/vaadin-grid-templatizer.html"),
      http("request_201")
        .get("/bower_components/vaadin-grid/vaadin-grid-table-scroll-behavior.html"),
      http("request_202")
        .get("/bower_components/vaadin-grid/vaadin-grid-sizer.html")))

  val afterLoginHtmlFiles5 =
    exec(http("Load html files after login (5/8)")
      .get("/bower_components/vaadin-grid/vaadin-grid-table-outer-scroller.html")
      .headers(headers_0)
      .resources(http("request_204")
        .get("/bower_components/vaadin-grid/vaadin-grid-table-header-footer.html"),
      http("request_205")
        .get("/bower_components/vaadin-grid/vaadin-grid-table-cell.html"),
      http("request_206")
        .get("/bower_components/vaadin-grid/vaadin-grid-table-focus-trap.html"),
      http("request_207")
        .get("/bower_components/vaadin-grid/vaadin-grid-table-row.html"),
      http("request_208")
        .get("/bower_components/vaadin-grid/vaadin-grid-keyboard-navigation-behavior.html"),
      http("request_209")
        .get("/bower_components/vaadin-grid/iron-list-behavior.html"),
      http("request_210")
        .get("/bower_components/polymer/lib/mixins/gesture-event-listeners.html"),
      http("request_211")
        .get("/bower_components/vaadin-themable-mixin/vaadin-themable-mixin.html"),
      http("request_212")
        .get("/bower_components/vaadin-control-state-mixin/vaadin-control-state-mixin.html"),
      http("request_213")
        .get("/bower_components/vaadin-text-field/vaadin-form-element-mixin.html"),
      http("request_214")
        .get("/bower_components/iron-iconset-svg/iron-iconset-svg.html"),
      http("request_215")
        .get("/src/storefront/storefront-item-badge.html"),
      http("request_216")
        .get("/bower_components/iron-form/iron-form.html"),
      http("request_217")
        .get("/bower_components/vaadin-text-field/vaadin-password-field.html"),
      http("request_218")
        .get("/bower_components/vaadin-form-layout/vaadin-form-layout.html"),
      http("request_219")
        .get("/bower_components/vaadin-form-layout/vaadin-form-item.html"),
      http("request_220")
        .get("/bower_components/vaadin-combo-box/vaadin-combo-box.html")))

  val afterLoginHtmlFiles6 =
    exec(http("Load html files after login (6/8)")
      .get("/bower_components/vaadin-combo-box/vaadin-combo-box-light.html")
      .headers(headers_0)
      .resources(
      http("request_222")
        .get("/bower_components/vaadin-date-picker/vaadin-date-picker.html"),
      http("request_223")
        .get("/src/elements/amount-field.html"),
      http("request_224")
        .get("/bower_components/vaadin-upload/vaadin-upload.html"),
      http("request_225")
        .get("/bower_components/iron-a11y-announcer/iron-a11y-announcer.html"),
      http("request_226")
        .get("/bower_components/iron-overlay-behavior/iron-overlay-behavior.html"),
      http("request_227")
        .get("/bower_components/font-roboto/roboto.html"),
      http("request_228")
        .get("/bower_components/iron-menu-behavior/iron-menu-behavior.html"),
      http("request_229")
        .get("/bower_components/paper-behaviors/paper-inky-focus-behavior.html"),
      http("request_230")
        .get("/bower_components/iron-a11y-keys-behavior/iron-a11y-keys-behavior.html"),
      http("request_231")
        .get("/bower_components/paper-ripple/paper-ripple.html"),
      http("request_232")
        .get("/bower_components/polymer/lib/utils/path.html"),
      http("request_233")
        .get("/bower_components/polymer/lib/mixins/property-accessors.html"),
      http("request_234")
        .get("/bower_components/polymer/lib/mixins/template-stamp.html"),
      http("request_235")
        .get("/bower_components/vaadin-valo-theme/font-icons.html"),
      http("request_236")
        .get("/bower_components/polymer/lib/legacy/class.html"),
      http("request_237")
        .get("/bower_components/polymer/lib/utils/templatize.html"),
      http("request_238")
        .get("/bower_components/shadycss/apply-shim.html"),
      http("request_239")
        .get("/bower_components/polymer/lib/utils/import-href.html"),
      http("request_240")
        .get("/bower_components/polymer/lib/utils/render-status.html"),
      http("request_241")
        .get("/bower_components/polymer/lib/utils/unresolved.html"),
      http("request_242")
        .get("/bower_components/polymer/lib/legacy/polymer.dom.html"),
      http("request_243")
        .get("/bower_components/polymer/lib/mixins/mutable-data.html"),
      http("request_244")
        .get("/bower_components/polymer/lib/utils/array-splice.html"),
      http("request_245")
        .get("/bower_components/shadycss/custom-style-interface.html"),
      http("request_246")
        .get("/bower_components/iron-scroll-target-behavior/iron-scroll-target-behavior.html"),
      http("request_247")
        .get("/bower_components/vaadin-grid/vaadin-grid-focusable-cell-container-behavior.html"),
      http("request_248")
        .get("/bower_components/vaadin-grid/vaadin-grid-cell-click-behavior.html"),
      http("request_249")
        .get("/bower_components/polymer/lib/utils/gestures.html"),
      http("request_250")
        .get("/bower_components/iron-ajax/iron-ajax.html")))

  val afterLoginHtmlFiles7 =
    exec(http("Load html files after login (7/8)")
      .get("/bower_components/vaadin-combo-box/vaadin-combo-box-overlay.html")
      .headers(headers_0)
      .resources(
      http("request_252")
        .get("/bower_components/vaadin-combo-box/vaadin-combo-box-mixin.html"),
      http("request_253")
        .get("/bower_components/vaadin-combo-box/vaadin-combo-box-styles.html"),
      http("request_254")
        .get("/bower_components/iron-dropdown/iron-dropdown.html"),
      http("request_255")
        .get("/bower_components/vaadin-date-picker/vaadin-date-picker-overlay.html"),
      http("request_256")
        .get("/bower_components/vaadin-date-picker/vaadin-date-picker-mixin.html"),
      http("request_257")
        .get("/bower_components/vaadin-date-picker/vaadin-date-picker-helper.html"),
      http("request_258")
        .get("/bower_components/vaadin-date-picker/vaadin-date-picker-styles.html"),
      http("request_259")
        .get("/bower_components/paper-button/paper-button.html"),
      http("request_260")
        .get("/bower_components/vaadin-upload/vaadin-upload-icons.html"),
      http("request_261")
        .get("/bower_components/vaadin-upload/vaadin-upload-file.html"),
      http("request_262")
        .get("/bower_components/iron-fit-behavior/iron-fit-behavior.html"),
      http("request_263")
        .get("/bower_components/iron-overlay-behavior/iron-overlay-manager.html"),
      http("request_264")
        .get("/bower_components/iron-overlay-behavior/iron-focusables-helper.html"),
      http("request_265")
        .get("/bower_components/iron-selector/iron-multi-selectable.html"),
      http("request_266")
        .get("/bower_components/polymer/lib/utils/flattened-nodes-observer.html"),
      http("request_267")
        .get("/bower_components/iron-ajax/iron-request.html")))

  val afterLoginHtmlFiles8 =
    exec(http("Load html files after login (8/8)")
      .get("/bower_components/vaadin-combo-box/vaadin-combo-box-item.html")
      .headers(headers_0)
      .resources(
      http("request_269")
        .get("/bower_components/vaadin-combo-box/vaadin-combo-box-spinner.html"),
      http("request_270")
        .get("/bower_components/iron-list/iron-list.html"),
      http("request_271")
        .get("/bower_components/vaadin-combo-box/vaadin-combo-box-dropdown.html"),
      http("request_273")
        .get("/bower_components/neon-animation/neon-animation-runner-behavior.html"),
      http("request_274")
        .get("/bower_components/iron-dropdown/iron-dropdown-scroll-manager.html"),
      http("request_275")
        .get("/bower_components/vaadin-button/vaadin-button.html"),
      http("request_276")
        .get("/bower_components/vaadin-date-picker/vaadin-month-calendar.html"),
      http("request_277")
        .get("/bower_components/vaadin-date-picker/vaadin-infinite-scroller.html"),
      http("request_278")
        .get("/bower_components/paper-behaviors/paper-button-behavior.html"),
      http("request_279")
        .get("/bower_components/paper-styles/element-styles/paper-material-styles.html"),
      http("request_280")
        .get("/bower_components/paper-progress/paper-progress.html"),
      http("request_281")
        .get("/bower_components/iron-overlay-behavior/iron-overlay-backdrop.html"),
      http("request_282")
        .get("/bower_components/iron-selector/iron-selectable.html"),
      http("request_283")
        .get("/bower_components/vaadin-overlay/vaadin-overlay.html"),
      http("request_284")
        .get("/bower_components/neon-animation/neon-animatable-behavior.html"),
      http("request_285")
        .get("/bower_components/paper-styles/shadow.html"),
      http("request_286")
        .get("/bower_components/iron-range-behavior/iron-range-behavior.html"),
      http("request_287")
        .get("/bower_components/iron-selector/iron-selection.html")))

  val dashboardHtmlFiles =
    exec(http("Load html files for Dashboard")
      .get("/src/dashboard/bakery-dashboard.html")
      .headers(headers_0)
      .resources(http("request_294")
          .get("/bower_components/vaadin-board/vaadin-board.html"),
        http("request_295")
          .get("/src/dashboard/dashboard-orders-counter.html"),
        http("request_296")
          .get("/bower_components/vaadin-board/vaadin-board-row.html"),
        http("request_297")
          .get("/src/dashboard/dashboard-column-chart.html"),
        http("request_298")
          .get("/src/dashboard/dashboard-yearly-sales.html"),
        http("request_299")
          .get("/src/dashboard/dashboard-pie-chart.html"),
        http("request_300")
          .get("/src/dashboard/dashboard-orders-list.html"),
        http("request_301")
          .get("/bower_components/vaadin-license-checker/vaadin-license-checker.html"),
        http("request_302")
          .get("/bower_components/vaadin-charts/vaadin-solidgauge-chart.html"),
        http("request_303")
          .get("/bower_components/vaadin-charts/vaadin-column-chart.html"),
        http("request_304")
          .get("/bower_components/vaadin-charts/vaadin-areaspline-chart.html"),
        http("request_305")
          .get("/bower_components/vaadin-charts/vaadin-pie-chart.html"),
        http("request_306")
          .get("/bower_components/iron-localstorage/iron-localstorage.html"),
        http("request_307")
          .get("/bower_components/vaadin-license-checker/vaadin-license-box.html"),
        http("request_308")
          .get("/bower_components/vaadin-license-checker/vaadin-license-dialog.html"),
        http("request_309")
          .get("/bower_components/vaadin-license-checker/vaadin-license-notification.html"),
        http("request_310")
          .get("/bower_components/vaadin-license-checker/vaadin-framework-identifier.html"),
        http("request_311")
          .get("/bower_components/vaadin-charts/data-series.html"),
        http("request_312")
          .get("/bower_components/vaadin-charts/base-chart.html"),
        http("request_313")
          .get("/bower_components/vaadin-charts/configuration-reader-mixin.html")))

  val scn = scenario("BaristaFlow")
    .exec(init)
    .pause(1)
    .randomSwitch(
       // no cache: load all html files
      (100-cacheRate) -> exec(initialHtmlFiles1)
        .pause(75 millisecond)
        .exec(initialHtmlFiles2)
        .pause(75 millisecond)
        .exec(initialHtmlFiles3)
        .pause(75 millisecond)
        .exec(initialHtmlFiles4)
        .pause(75 millisecond)

        .exec(attachNavigation)
        .pause(8, 11)
        .exec(performLogin)

        .exec(afterLoginHtmlFiles1)
        .pause(75 millisecond)
        .exec(afterLoginHtmlFiles2)
        .pause(75 millisecond)
        .exec(afterLoginHtmlFiles3)
        .pause(75 millisecond)
        .exec(afterLoginHtmlFiles4)
        .pause(75 millisecond)
        .exec(afterLoginHtmlFiles5)
        .pause(75 millisecond)
        .exec(afterLoginHtmlFiles6)
        .pause(75 millisecond)
        .exec(afterLoginHtmlFiles7)
        .pause(75 millisecond)
        .exec(afterLoginHtmlFiles8)
        .pause(75 millisecond)

        .pause(1)
        .exec(attachSearchAndNavigation)
        .pause(1)
        .exec(attachButtons)
        .pause(5, 10)
        .exec(clickNewOrder)
        .pause(45, 55)
        .exec(submitOrderForm)
        .pause(10)
        .exec(navigateToDashboard)
        .exec(dashboardHtmlFiles)
        .pause(1)
        .exec(loadOrdersPage1)
        .pause(1)
        .exec(loadOrdersPage2)
        .pause(30)
        .exec(navigateToStorefront),

      // "cache" do not load html files
      cacheRate -> exec(attachNavigation)
        .pause(8, 11)
        .exec(performLogin)
        .pause(1)
        .exec(attachSearchAndNavigation)
        .pause(1)
        .exec(attachButtons)
        .pause(5, 10)
        .exec(clickNewOrder)
        .pause(45, 55)
        .exec(submitOrderForm)
        .pause(10)
        .exec(navigateToDashboard)
        .pause(1)
        .exec(loadOrdersPage1)
        .pause(1)
        .exec(loadOrdersPage2)
        .pause(30)
        .exec(navigateToStorefront)
    )

  setUp(scn.inject(rampUsers(1) over (1 seconds))).protocols(httpProtocol)
}