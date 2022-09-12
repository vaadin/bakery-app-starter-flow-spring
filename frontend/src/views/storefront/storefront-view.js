import { unsafeCSS } from "lit-element";
import { html, LitElement, css } from "lit-element";

import "@vaadin/vaadin-grid/src/vaadin-grid.js";
import "@vaadin/vaadin-dialog/src/vaadin-dialog.js";
import "../../components/search-bar.js";
import "../../components/utils-mixin.js";
import "./order-card.js";
import "../../../styles/shared-styles.js";

class StorefrontView extends LitElement {
  static get styles() {
    const includedStyles = {};
    includedStyles["shared-styles"] =
      document.querySelector("dom-module[id='shared-styles']") &&
      document.querySelector("dom-module[id='shared-styles']")
        .firstElementChild &&
      document.querySelector("dom-module[id='shared-styles']").firstElementChild
        .content &&
      document.querySelector("dom-module[id='shared-styles']").firstElementChild
        .content.firstElementChild &&
      document.querySelector("dom-module[id='shared-styles']").firstElementChild
        .content.firstElementChild.innerText
        ? document.querySelector("dom-module[id='shared-styles']")
            .firstElementChild.content.firstElementChild.innerText
        : "";
    return [
      unsafeCSS(includedStyles["shared-styles"]),
      css`
        :host {
          display: flex;
          flex-direction: column;
          height: 100%;
        }
      `,
    ];
  }
  render() {
    return html`
      <search-bar id="search" show-checkbox=""></search-bar>

      <vaadin-grid id="grid" theme="orders no-row-borders"></vaadin-grid>

      <vaadin-dialog
        id="dialog"
        theme="orders"
        @opened-changed="${this._onDialogOpen}"
      ></vaadin-dialog>
    `;
  }

  static get is() {
    return "storefront-view";
  }

  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);

    // This code is needed to measure the page load performance and can be safely removed
    // if there is no need for that.
    const grid = this.renderRoot.querySelector("#grid");
    const listener = () => {
      if (!grid.loading && window.performance.mark) {
        window.performance.mark("bakery-page-loaded");
        grid.removeEventListener("loading-changed", listener);
      }
    };
    grid.addEventListener("loading-changed", listener);
  }

  // Workaround for styling the dialog content https://github.com/vaadin/vaadin-dialog-flow/issues/69
  _onDialogOpen(e) {
    if (!e.detail.value) {
      return;
    }
    var content = this.renderRoot.querySelector("#dialog").$.overlay.content;
    content
      .querySelector("flow-component-renderer")
      .setAttribute("theme", "dialog");
  }
}

window.customElements.define(StorefrontView.is, StorefrontView);
