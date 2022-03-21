import { html, css, LitElement } from 'lit';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/integer-field/src/vaadin-integer-field.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/button/src/vaadin-button.js';
import '@vaadin/form-layout/src/vaadin-form-layout.js';
import '@vaadin/combo-box/src/vaadin-combo-box.js';
import '../../../styles/shared-styles.js';

class OrderItemEditor extends LitElement {
  static get styles() {
    return css`
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
    `;
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
            has-controls
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
          <iron-icon icon="vaadin:close-small"></iron-icon>
        </vaadin-button>
      </vaadin-form-layout>
    `;
  }

  static get is() {
    return 'order-item-editor';
  }

  constructor() {
    super();

    this.form1responsiveSteps = [{ columns: 24 }];
    this.form2responsiveSteps = [
      { columns: 8, labelsPosition: 'top' },
      { minWidth: '500px', columns: 16, labelsPosition: 'top' },
    ];
  }
}

window.customElements.define(OrderItemEditor.is, OrderItemEditor);
