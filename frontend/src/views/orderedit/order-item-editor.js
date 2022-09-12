import { unsafeCSS } from "lit-element";
import { html, LitElement, css } from "lit-element";

import "@polymer/iron-icon/iron-icon.js";
import "@vaadin/vaadin-text-field/src/vaadin-integer-field.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-layout.js";
import "@vaadin/vaadin-combo-box/src/vaadin-combo-box.js";
import "../../../styles/shared-styles.js";

class OrderItemEditor extends LitElement {
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
        .product {
          margin-bottom: 1em;
        }

        .delete {
          min-width: 2em;
          padding: 0;
        }

        @media (max-width: 700px) {
          vaadin-form-layout {
            --vaadin-form-layout-column-spacing: 1em;
          }
        }

        .money {
          text-align: right;
          line-height: 2.5em;
        }

        /* Workaround for vertically distorted elements inside a flex container in IE11 */
        .self-start {
          align-self: flex-start;
        }
      `,
    ];
  }
  render() {
    return html`
      <vaadin-form-layout id="form1">
        <vaadin-form-layout
          id="form2"
          colspan="16"
          class="product"
          style="flex: auto;"
        >
          <vaadin-combo-box
            id="products"
            colspan="8"
            .index="${this.index}"
          ></vaadin-combo-box>
          <vaadin-integer-field
            id="amount"
            colspan="4"
            class="self-start"
            min="1"
            max="15"
            has-controls
            prevent-invalid-input
            .index="${this.index}"
          ></vaadin-integer-field>
          <div id="price" colspan="4" class="money">${this.price}</div>
          <vaadin-text-field
            id="comment"
            colspan="12"
            placeholder="Details"
            .index="${this.index}"
          ></vaadin-text-field>
        </vaadin-form-layout>

        <vaadin-button
          class="delete self-start"
          id="delete"
          colspan="2"
          .index="${this.index}"
        >
          <iron-icon icon="vaadin:close-small"></iron-icon>
        </vaadin-button>
      </vaadin-form-layout>
    `;
  }

  static get is() {
    return "order-item-editor";
  }

  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);

    // Not using attributes since Designer does not suppor single-quote attributes
    this.renderRoot.querySelector("#form1").responsiveSteps = [{ columns: 24 }];
    this.renderRoot.querySelector("#form2").responsiveSteps = [
      { columns: 8, labelsPosition: "top" },
      { minWidth: "500px", columns: 16, labelsPosition: "top" },
    ];
  }
}

window.customElements.define(OrderItemEditor.is, OrderItemEditor);
