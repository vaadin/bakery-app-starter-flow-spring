import { html, css, LitElement } from 'lit';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/icons/vaadin-icons.js';

class OrderStatusBadge extends LitElement {
  static get styles() {
    return css`
      #wrapper {
        display: inline-block;
        border-radius: var(--lumo-border-radius);
        background: var(--lumo-shade-10pct);
        color: var(--lumo-secondary-text-color);
        padding: 2px 10px;
        font-size: var(--lumo-font-size-xs);
        text-transform: capitalize;
      }

      :host([status='ready']) #wrapper {
        color: var(--lumo-success-color);
        background: var(--lumo-success-color-10pct);
      }

      :host([status='new']) #wrapper {
        color: var(--lumo-primary-color);
        background: var(--lumo-primary-color-10pct);
      }

      :host([status='problem']) #wrapper {
        color: var(--lumo-error-color);
        background: var(--lumo-error-color-10pct);
      }

      :host([status='delivered']) #wrapper {
        padding: 2px 8px;
      }

      :host([status='delivered']) #wrapper span,
      :host(:not([status='delivered'])) #wrapper iron-icon {
        display: none;
      }

      :host([small]) #wrapper {
        padding: 0 5px;
      }

      iron-icon {
        --iron-icon-width: 12px;
      }

      :host([small]) iron-icon {
        --iron-icon-width: 8px;
      }
    `;
  }

  render() {
    return html`
      <div id="wrapper">
        <span>${this.__toLowerCase(this.status)}</span>
        <iron-icon icon="vaadin:check"></iron-icon>
      </div>
    `;
  }

  static get is() {
    return 'order-status-badge';
  }

  static get properties() {
    return {
      status: {
        type: String,
        reflect: true,
        converter: {
          fromAttribute: (value) => value.toUpperCase(),
          toAttribute: (value) => value.toLowerCase()
        }
      }
    };
  }

  __toLowerCase(status) {
    return status ? status.toLowerCase() : '';
  }
}

customElements.define(OrderStatusBadge.is, OrderStatusBadge);
