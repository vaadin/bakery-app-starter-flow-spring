import { html, LitElement, css } from "lit-element";

import "@polymer/iron-ajax/iron-ajax.js";
class OfflineBanner extends LitElement {
  static get styles() {
    return [
      css`
        .offline[hidden] {
          display: none !important;
        }
      `,
    ];
  }
  render() {
    return html`
      <iron-ajax
        auto
        url="./offline-page.html"
        handle-as="document"
        .last-response="${this.offlinePage}"
        @last-response-changed="${(e) => (this.offlinePage = e.target.value)}"
      ></iron-ajax>

      <div id="offline" class="offline" ?hidden="${this.online}"></div>
    `;
  }

  static get is() {
    return "offline-banner";
  }

  static get observers() {
    return ["_offlinePageChanged(offlinePage)"];
  }

  // Reusing offline-page.html content in order not to duplicate.
  // The page is requested using iron-ajax.
  _offlinePageChanged(doc) {
    if (doc) {
      this.renderRoot
        .querySelector("#offline")
        .appendChild(doc.querySelector("style"));
      this.renderRoot
        .querySelector("#offline")
        .appendChild(doc.querySelector(".content"));
    }
  }

  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);

    // This might be provided by flow in the future (#3778)
    this.online = window.navigator.onLine;
    window.addEventListener("online", (e) => (this.online = true));
    window.addEventListener("offline", (e) => (this.online = false));
  }
}

window.customElements.define(OfflineBanner.is, OfflineBanner);
