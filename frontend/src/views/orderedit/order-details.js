import { html, css, LitElement } from 'lit';
import { map } from 'lit/directives/map.js';
import { when } from 'lit/directives/when.js';
import '@vaadin/icons/vaadin-icons.js';
import '@vaadin/button';
import '@vaadin/form-layout';
import '@vaadin/form-layout/vaadin-form-item.js';
import '@vaadin/icon';
import '@vaadin/icons';
import '@vaadin/text-field';
import '../../components/buttons-bar.js';
import { ScrollShadowMixin } from '../../components/utils-mixin.js';
import '../storefront/order-status-badge.js';
import { sharedStyles } from '../../../styles/shared-styles.js';

class OrderDetails extends ScrollShadowMixin(LitElement) {
  static get styles() {
    return [
      sharedStyles,
      css`
        :host {
          display: flex;
          flex-direction: column;
          box-sizing: border-box;
          flex: auto;
        }

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
            .status="${this.item && this.item.state}"
          ></order-status-badge>
          <span class="dim">Order #${this.item && this.item.id}</span>
        </div>

        <vaadin-form-layout
          id="form1"
          .responsiveSteps="${this.form1responsiveSteps}"
        >
          <vaadin-form-item>
            <label slot="label">Due</label>
            <vaadin-form-layout
              id="form2"
              .responsiveSteps="${this.form2responsiveSteps}"
            >
              <div class="date">
                <h3>${this.item && this.item.formattedDueDate.day}</h3>
                <span class="dim"
                  >${this.item && this.item.formattedDueDate.weekday}</span
                >
              </div>
              <div class="time">
                <h3>${this.item && this.item.formattedDueTime}</h3>
                <span class="dim"
                  >${this.item && this.item.pickupLocation.name}</span
                >
              </div>
            </vaadin-form-layout>
          </vaadin-form-item>

          <vaadin-form-item colspan="2">
            <label slot="label">Customer</label>
            <h3>${this.item && this.item.customer.fullName}</h3>
          </vaadin-form-item>

          <vaadin-form-item>
            <label slot="label">Phone number</label>
            <h3>${this.item && this.item.customer.phoneNumber}</h3>
          </vaadin-form-item>
        </vaadin-form-layout>

        <vaadin-form-layout
          id="form3"
          .responsiveSteps="${this.form3responsiveSteps}"
        >
          <div></div>

          <vaadin-form-layout
            id="form4"
            colspan="2"
            .responsiveSteps="${this.form4responsiveSteps}"
          >
            ${when(this.item && this.item.customer.details, () => html`
                <vaadin-form-item label-position="top">
                  <label slot="label">Additional details</label>
                  <span>${this.item.customer.details}</span>
                </vaadin-form-item>`)}

            <vaadin-form-item>
              <label slot="label">Products</label>
              <div class="table products">
                ${this.item &&
                map(this.item.items, (item) => {
                  return (
                    item.product.name &&
                    html`
                      <div class="tr">
                        <div class="td product-name">
                          <div class="bold">${item.product.name}</div>
                          <div class="secondary">${item.comment}</div>
                        </div>
                        <div class="td number">
                          <span class="count">${item.quantity}</span>
                        </div>
                        <div class="td dim">×</div>
                        <div class="td money">
                          ${item.product.formattedPrice}
                        </div>
                      </div>
                    `
                  );
                })}
              </div>
            </vaadin-form-item>

            <vaadin-form-item
              id="history"
              .labelPosition="${'top'}"
              .hidden="${this.review}"
            >
              <label slot="label">History</label>
              ${this.item &&
              map(this.item.history, (event) => html`
                  <div class="history-line">
                    <span class="bold">${event.createdBy.firstName}</span>
                    <span class="secondary">${event.formattedTimestamp}</span>
                    <order-status-badge
                      .status="${event.newState}"
                      small=""
                    ></order-status-badge>
                  </div>
                  <div class="comment">${event.message}</div>
                `
              )}
            </vaadin-form-item>

            <vaadin-form-item id="comment" .hidden="${this.review}">
              <vaadin-text-field
                id="commentField"
                placeholder="Add comment"
                class="full-width"
                @keydown="${this._onCommentKeydown}"
                maxlength="255"
              >
                <div slot="suffix" class="comment-suffix">
                  <vaadin-button id="sendComment" theme="tertiary">
                    Send
                  </vaadin-button>
                </div>
              </vaadin-text-field>
            </vaadin-form-item>
          </vaadin-form-layout>
        </vaadin-form-layout>
      </div>

      <buttons-bar id="footer" no-scroll="${this.noScroll}">
        <vaadin-button slot="left" id="back" .hidden="${!this.review}">
          Back
        </vaadin-button>
        <vaadin-button slot="left" id="cancel" .hidden="${this.review}">
          Cancel
        </vaadin-button>

        <div slot="info" class="total">
          Total ${this.item && this.item.formattedTotalPrice}
        </div>

        <vaadin-button
          slot="right"
          id="save"
          theme="primary success"
          .hidden="${!this.review}"
        >
          <vaadin-icon icon="vaadin:check" slot="suffix"></vaadin-icon>
          Place order
        </vaadin-button>
        <vaadin-button
          slot="right"
          id="edit"
          theme="primary"
          .hidden="${this.review}"
        >
          Edit order
          <vaadin-icon icon="vaadin:edit" slot="suffix"></vaadin-icon>
        </vaadin-button>
      </buttons-bar>
    `;
  }

  static get is() {
    return 'order-details';
  }

  static get properties() {
    return {
      item: {
        type: Object,
      },
      review: {
        type: Boolean,
      },
      form1responsiveSteps: {
        type: Array,
      },
      form2responsiveSteps: {
        type: Array,
      },
      form3responsiveSteps: {
        type: Array,
      },
    };
  }

  constructor() {
    super();

    /** @type {{ minWidth: string | 0; columns: number; labelsPosition: "top" | "aside"; }[]} */
    this.form1responsiveSteps = this.form3responsiveSteps = [
      { columns: 1, labelsPosition: 'top' },
      { minWidth: '600px', columns: 4, labelsPosition: 'top' },
    ];
    /** @type {{ minWidth: string | 0; columns: number; labelsPosition: "top" | "aside"; }[]} */
    this.form2responsiveSteps = [
      { columns: 1 },
      { minWidth: '180px', columns: 2 },
    ];
    /** @type {{ minWidth: string | 0; columns: number; labelsPosition: "top" | "aside"; }[]} */
    this.form4responsiveSteps = [{ columns: 1, labelsPosition: 'top' }];
  }

  _onCommentKeydown(event) {
    if (event.key === 'Enter' || event.keyCode == 13) {
      this.shadowRoot.querySelector('#commentField').blur();
      this.shadowRoot.querySelector('#sendComment').click();
    }
  }
}

customElements.define(OrderDetails.is, OrderDetails);
