import { unsafeCSS } from "lit-element";
import { html, LitElement, css } from "lit-element";

import "@polymer/iron-icon/iron-icon.js";
import "@vaadin/vaadin-icons/vaadin-icons.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-item.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-layout.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "../../components/buttons-bar.js";
import "../../components/utils-mixin.js";
import "../storefront/order-status-badge.js";
import "../../../styles/shared-styles.js";

class OrderDetails extends window.ScrollShadowMixin(LitElement) {
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
          box-sizing: border-box;
          flex: auto;
        }

        /*
        Workaround for non-working dom-repeat inside tables in IE11
        (https://github.com/Polymer/polymer/issues/1567):
        use divs with table-like display values instead of the actual
        <table>, <tr> and <td> elements.
      */
        .table {
          display: table;
        }

        .tr {
          display: table-row;
        }

        .td {
          display: table-cell;
        }

        .main-row {
          flex: 1;
        }

        h3 {
          margin: 0;
          word-break: break-all;
          /* Non standard for WebKit */
          word-break: break-word;
          white-space: normal;
        }

        .date,
        .time {
          white-space: nowrap;
        }

        .dim,
        .secondary {
          color: var(--lumo-secondary-text-color);
        }

        .secondary {
          font-size: var(--lumo-font-size-xs);
          line-height: var(--lumo-font-size-xl);
        }

        .meta-row {
          display: flex;
          justify-content: space-between;
          padding-bottom: var(--lumo-space-s);
        }

        .products {
          width: 100%;
        }

        .products .td {
          text-align: center;
          vertical-align: middle;
          padding: var(--lumo-space-xs);
          border: none;
          border-bottom: 1px solid var(--lumo-contrast-10pct);
        }

        .products .td.product-name {
          text-align: left;
          padding-left: 0;
          width: 100%;
        }

        .products .td.number {
          text-align: right;
        }

        .products .td.money {
          text-align: right;
          padding-right: 0;
        }

        .history-line {
          margin: var(--lumo-space-xs) 0;
        }

        .comment {
          font-size: var(--lumo-font-size-s);
        }

        order-status-badge[small] {
          margin-left: 0.5em;
        }

        #sendComment {
          color: var(--lumo-primary-color-50pct);
        }

        @media (min-width: 600px) {
          .main-row {
            padding: var(--lumo-space-l);
            flex-basis: auto;
          }
        }
      `,
    ];
  }
  render() {
    return html`
      <div class="scrollable main-row" id="main">
        <div class="meta-row">
          <order-status-badge
            .status="${this.item ? this.item.state : undefined}"
          ></order-status-badge>
          <span class="dim"
            >Order #${this.item ? this.item.id : undefined}</span
          >
        </div>

        <vaadin-form-layout id="form1">
          <vaadin-form-item>
            <label slot="label">Due</label>
            <vaadin-form-layout id="form2">
              <div class="date">
                <h3>
                  ${this.item && this.item.dueDate
                    ? this.item.dueDate.day
                    : undefined}
                </h3>
                <span class="dim"
                  >${this.item && this.item.dueDate
                    ? this.item.dueDate.weekday
                    : undefined}</span
                >
              </div>
              <div class="time">
                <h3>${this.item ? this.item.dueTime : undefined}</h3>
                <span class="dim"
                  >${this.item && this.item.pickupLocation
                    ? this.item.pickupLocation.name
                    : undefined}</span
                >
              </div>
            </vaadin-form-layout>
          </vaadin-form-item>

          <vaadin-form-item colspan="2">
            <label slot="label">Customer</label>
            <h3>
              ${this.item && this.item.customer
                ? this.item.customer.fullName
                : undefined}
            </h3>
          </vaadin-form-item>

          <vaadin-form-item>
            <label slot="label">Phone number</label>
            <h3>
              ${this.item && this.item.customer
                ? this.item.customer.phoneNumber
                : undefined}
            </h3>
          </vaadin-form-item>
        </vaadin-form-layout>

        <vaadin-form-layout id="form3">
          <div></div>

          <vaadin-form-layout id="form4" colspan="2">
            ${this.item && this.item.customer
              ? this.item.customer.details
              : undefined
              ? html`
                  <vaadin-form-item label-position="top">
                    <label slot="label">Additional details</label>
                    <span
                      >${this.item && this.item.customer
                        ? this.item.customer.details
                        : undefined}</span
                    >
                  </vaadin-form-item>
                `
              : html``}

            <vaadin-form-item>
              <label slot="label">Products</label>
              <div class="table products">
                ${(this.item && this.item.items ? this.item.items : []).map(
                  (item, index) => html`
                    ${item.product
                      ? item.product.name
                      : undefined
                      ? html`
                          <div class="tr">
                            <div class="td product-name">
                              <div class="bold">
                                ${item.product ? item.product.name : undefined}
                              </div>
                              <div class="secondary">${item.comment}</div>
                            </div>
                            <div class="td number">
                              <span class="count">${item.quantity}</span>
                            </div>
                            <div class="td dim">×</div>
                            <div class="td money">
                              ${item.product ? item.product.price : undefined}
                            </div>
                          </div>
                        `
                      : html``}
                  `
                )}
              </div>
            </vaadin-form-item>

            <vaadin-form-item
              id="history"
              label-position="top"
              .hidden="${this.review}"
            >
              <label slot="label">History</label>
              ${(this.item && this.item.history ? this.item.history : []).map(
                (item, index) => html`
                  <div class="history-line">
                    <span class="bold"
                      >${this.event && this.event.createdBy
                        ? this.event.createdBy.firstName
                        : undefined}</span
                    >
                    <span class="secondary"
                      >${this.event ? this.event.timestamp : undefined}</span
                    >
                    <order-status-badge
                      small=""
                      .status="${this.event ? this.event.newState : undefined}"
                    ></order-status-badge>
                  </div>
                  <div class="comment">
                    ${this.event ? this.event.message : undefined}
                  </div>
                `
              )}
            </vaadin-form-item>

            <vaadin-form-item id="comment" .hidden="${this.review}">
              <vaadin-text-field
                id="commentField"
                placeholder="Add comment"
                class="full-width"
                maxlength="255"
                @keydown="${this._onCommentKeydown}"
              >
                <div slot="suffix" class="comment-suffix">
                  <vaadin-button id="sendComment" theme="tertiary"
                    >Send</vaadin-button
                  >
                </div>
              </vaadin-text-field>
            </vaadin-form-item>
          </vaadin-form-layout>
        </vaadin-form-layout>
      </div>

      <buttons-bar id="footer" no-scroll="${this.noScroll}">
        <vaadin-button slot="left" id="back" .hidden="${!this.review}"
          >Back</vaadin-button
        >
        <vaadin-button slot="left" id="cancel" .hidden="${this.review}"
          >Cancel</vaadin-button
        >

        <div slot="info" class="total">
          Total ${this.item ? this.item.totalPrice : undefined}
        </div>

        <vaadin-button
          slot="right"
          id="save"
          theme="primary success"
          .hidden="${!this.review}"
        >
          <iron-icon icon="vaadin:check" slot="suffix"></iron-icon>
          Place order</vaadin-button
        >
        <vaadin-button
          slot="right"
          id="edit"
          theme="primary"
          .hidden="${this.review}"
        >
          Edit order
          <iron-icon icon="vaadin:edit" slot="suffix"></iron-icon>
        </vaadin-button>
      </buttons-bar>
    `;
  }

  static get is() {
    return "order-details";
  }

  static get properties() {
    return {
      item: {
        type: Object,
      },
    };
  }

  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);

    this.renderRoot.querySelector(
      "#form1"
    ).responsiveSteps = this.renderRoot.querySelector(
      "#form3"
    ).responsiveSteps = [
      { columns: 1, labelsPosition: "top" },
      { minWidth: "600px", columns: 4, labelsPosition: "top" },
    ];

    this.renderRoot.querySelector("#form2").responsiveSteps = [
      { columns: 1 },
      { minWidth: "180px", columns: 2 },
    ];

    this.renderRoot.querySelector("#form4").responsiveSteps = [
      { columns: 1, labelsPosition: "top" },
    ];
  }

  _onCommentKeydown(event) {
    if (event.key === "Enter" || event.keyCode == 13) {
      // In IE11 on button click commentField blur doesn't happen, and the value-change event is not fired
      this.renderRoot.querySelector("#commentField").blur();
      this.renderRoot.querySelector("#sendComment").click();
    }
  }
}

window.customElements.define(OrderDetails.is, OrderDetails);
