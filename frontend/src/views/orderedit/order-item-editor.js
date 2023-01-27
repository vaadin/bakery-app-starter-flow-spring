import { html, css, LitElement } from 'lit';
import '@vaadin/button';
import '@vaadin/combo-box';
import '@vaadin/form-layout';
import '@vaadin/icon';
import '@vaadin/icons';
import '@vaadin/integer-field';
import '@vaadin/text-field';
import { sharedStyles } from '../../../styles/shared-styles.js';

class OrderItemEditor extends LitElement {
  static get styles() {
    return [
      sharedStyles,
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
      <vaadin-form-layout
        id="form1"
        .responsiveSteps="${this.form1responsiveSteps}"
      >
        <vaadin-form-layout
          id="form2"
          colspan="16"
          class="product"
          style="flex: auto;"
          .responsiveSteps="${this.form2responsiveSteps}"
        >
          <vaadin-combo-box id="products" colspan="8"></vaadin-combo-box>
          <vaadin-integer-field
            id="amount"
            colspan="4"
            class="self-start"
            min="1"
            max="15"
            step-buttons-visible
            prevent-invalid-input
          ></vaadin-integer-field>
          <div id="price" colspan="4" class="money"></div>
          <vaadin-text-field
            id="comment"
            colspan="12"
            placeholder="Details"
          ></vaadin-text-field>
        </vaadin-form-layout>

        <vaadin-button class="delete self-start" id="delete" colspan="2">
          <vaadin-icon icon="vaadin:close-small"></vaadin-icon>
        </vaadin-button>
      </vaadin-form-layout>
    `;
  }

  static get is() {
    return 'order-item-editor';
  }

  constructor() {
    super();

    /** @type {{ minWidth: string | 0; columns: number; labelsPosition: "top" | "aside"; }[]} */
    this.form1responsiveSteps = [{ columns: 24 }];
    /** @type {{ minWidth: string | 0; columns: number; labelsPosition: "top" | "aside"; }[]} */
    this.form2responsiveSteps = [
      { columns: 8, labelsPosition: 'top' },
      { minWidth: '500px', columns: 16, labelsPosition: 'top' },
    ];
  }
}

customElements.define(OrderItemEditor.is, OrderItemEditor);
