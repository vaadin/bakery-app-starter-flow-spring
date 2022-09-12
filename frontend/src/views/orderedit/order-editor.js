import { unsafeCSS } from "lit-element";
import { html, LitElement, css } from "lit-element";

import "@polymer/iron-icon/iron-icon.js";
import "@vaadin/vaadin-icons/vaadin-icons.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-layout.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-item.js";
import "@vaadin/vaadin-combo-box/src/vaadin-combo-box.js";
import "@vaadin/vaadin-date-picker/src/vaadin-date-picker.js";
import "../../components/buttons-bar.js";
import "../../components/utils-mixin.js";
import "./order-item-editor.js";
import "../../../styles/shared-styles.js";

class OrderEditor extends window.ScrollShadowMixin(LitElement) {
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
          flex: auto;
        }

        .meta-row {
          display: flex;
          justify-content: space-between;
          padding-bottom: var(--lumo-space-s);
        }

        .dim {
          color: var(--lumo-secondary-text-color);
          text-align: right;
          white-space: nowrap;
          line-height: 2.5em;
        }

        .status {
          width: 10em;
        }
      `,
    ];
  }
  render() {
    return html`
      <div class="scrollable flex1" id="main">
        <h2 id="title">New order</h2>

        <div class="meta-row" id="metaContainer">
          <vaadin-combo-box class="status" id="status"></vaadin-combo-box>
          <span class="dim">Order #<span id="orderNumber"></span></span>
        </div>

        <vaadin-form-layout id="form1">
          <vaadin-form-layout id="form2">
            <vaadin-date-picker label="Due" id="dueDate"> </vaadin-date-picker>
            <vaadin-combo-box id="dueTime">
              <iron-icon slot="prefix" icon="vaadin:clock"></iron-icon>
            </vaadin-combo-box>
            <vaadin-combo-box id="pickupLocation" colspan="2">
              <iron-icon slot="prefix" icon="vaadin:at"></iron-icon>
            </vaadin-combo-box>
          </vaadin-form-layout>

          <vaadin-form-layout id="form3" colspan="3">
            <vaadin-text-field id="customerName" label="Customer" colspan="2">
              <iron-icon slot="prefix" icon="vaadin:user"></iron-icon>
            </vaadin-text-field>

            <vaadin-text-field id="customerNumber" label="Phone number">
              <iron-icon slot="prefix" icon="vaadin:phone"></iron-icon>
            </vaadin-text-field>

            <vaadin-text-field
              id="customerDetails"
              label="Additional Details"
              colspan="2"
            ></vaadin-text-field>

            <vaadin-form-item colspan="3">
              <label slot="label">Products</label>
            </vaadin-form-item>
            <div id="itemsContainer" colspan="3"></div>
          </vaadin-form-layout>
        </vaadin-form-layout>
      </div>

      <buttons-bar id="footer" no-scroll="${this.noScroll}">
        <vaadin-button slot="left" id="cancel">Cancel</vaadin-button>
        <div slot="info" class="total">Total ${this.totalPrice}</div>
        <vaadin-button slot="right" id="review" theme="primary">
          Review order
          <iron-icon icon="vaadin:arrow-right" slot="suffix"></iron-icon>
        </vaadin-button>
      </buttons-bar>
    `;
  }

  static get is() {
    return "order-editor";
  }

  static get properties() {
    return {
      status: {
        type: String,
      },
    };
  }

  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);

    // Not using attributes since Designer does not suppor single-quote attributes
    this.renderRoot.querySelector("#form1").responsiveSteps = [
      { columns: 1, labelsPosition: "top" },
      { minWidth: "600px", columns: 4, labelsPosition: "top" },
    ];
    this.renderRoot.querySelector("#form2").responsiveSteps = [
      { columns: 1, labelsPosition: "top" },
      { minWidth: "360px", columns: 2, labelsPosition: "top" },
    ];
    this.renderRoot.querySelector("#form3").responsiveSteps = [
      { columns: 1, labelsPosition: "top" },
      { minWidth: "500px", columns: 3, labelsPosition: "top" },
    ];
  }

  _onStatusChange() {
    const status = this.status ? this.status.toLowerCase() : this.status;
    this.renderRoot
      .querySelector("#status")
      .$.input.setAttribute("status", status);
  }
  set status(newValue) {
    const oldValue = this.status;
    this._status = newValue;
    if (oldValue !== newValue) {
      this._onStatusChange(newValue, oldValue);
      this.requestUpdateInternal(
        "status",
        oldValue,
        this.constructor.properties.status
      );
    }
  }
  get status() {
    return this._status;
  }
}

window.customElements.define(OrderEditor.is, OrderEditor);
