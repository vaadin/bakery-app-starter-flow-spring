import { html, css, LitElement } from 'lit';
import '@vaadin/button';
import '@vaadin/checkbox';
import '@vaadin/icon';
import '@vaadin/icons';
import '@vaadin/text-field';

class SearchBar extends LitElement {
  static get styles() {
    return css`
      :host {
        position: relative;
        z-index: 2;
        display: flex;
        flex-direction: column;
        overflow: hidden;
        padding: 0 var(--lumo-space-s);
        background-image: linear-gradient(
          var(--lumo-shade-20pct),
          var(--lumo-shade-20pct)
        );
        background-color: var(--lumo-base-color);
        box-shadow: 0 0 16px 2px var(--lumo-shade-20pct);
        order: 1;
        width: 100%;
        height: 48px;
        box-sizing: border-box;
      }

      .row {
        display: flex;
        align-items: center;
        height: 3em;
      }

      .checkbox,
      .clear-btn,
      :host([show-extra-filters]) .action-btn {
        display: none;
      }

      :host([show-extra-filters]) .clear-btn {
        display: block;
      }

      :host([show-checkbox]) .checkbox.mobile {
        display: block;
        transition: all 0.5s;
        height: 0;
      }

      :host([show-checkbox][show-extra-filters]) .checkbox.mobile {
        height: 2em;
      }

      .field {
        flex: 1;
        width: auto;
        padding-right: var(--lumo-space-s);
      }

      @media (min-width: 700px) {
        :host {
          order: 0;
        }

        .row {
          width: 100%;
          max-width: 964px;
          margin: 0 auto;
        }

        .field {
          padding-right: var(--lumo-space-m);
        }

        :host([show-checkbox][show-extra-filters]) .checkbox.desktop {
          display: block;
        }

        :host([show-checkbox][show-extra-filters]) .checkbox.mobile {
          display: none;
        }
      }
    `;
  }

  render() {
    return html`
      <div class="row">
        <vaadin-text-field
          id="field"
          class="field"
          .placeholder="${this.fieldPlaceholder}"
          .value="${this.fieldValue}"
          @value-changed="${(e) => (this.fieldValue = e.detail.value)}"
          @focus="${this._onFieldFocus}"
          @blur="${this._onFieldBlur}"
          theme="white"
        >
          <vaadin-icon icon="${this.fieldIcon}" slot="prefix"></vaadin-icon>
        </vaadin-text-field>

        <vaadin-checkbox
          class="checkbox desktop"
          .checked="${this.checkboxChecked}"
          @checked-changed="${(e) => (this.checkboxChecked = e.detail.value)}"
          @focus="${this._onFieldFocus}"
          @blur="${this._onFieldBlur}"
          .label="${this.checkboxText}"
        ></vaadin-checkbox>

        <vaadin-button id="clear" class="clear-btn" theme="tertiary">
          ${this.clearText}
        </vaadin-button>

        <vaadin-button id="action" class="action-btn" theme="primary">
          <vaadin-icon icon="${this.buttonIcon}" slot="prefix"></vaadin-icon>
          ${this.buttonText}
        </vaadin-button>
      </div>

      <vaadin-checkbox
        class="checkbox mobile"
        .checked="${this.checkboxChecked}"
        @checked-changed="${(e) => (this.checkboxChecked = e.detail.value)}"
        @focus="${this._onFieldFocus}"
        @blur="${this._onFieldBlur}"
        .label="${this.checkboxText}"
      ></vaadin-checkbox>
    `;
  }

  static get is() {
    return 'search-bar';
  }
  static get properties() {
    return {
      fieldPlaceholder: {
        type: String,
      },
      fieldValue: {
        type: String,
      },
      fieldIcon: {
        type: String,
      },
      buttonIcon: {
        type: String,
      },
      buttonText: {
        type: String,
      },
      showCheckbox: {
        type: Boolean,
        reflect: true,
        attribute: 'show-checkbox',
      },
      checkboxText: {
        type: String,
      },
      checkboxChecked: {
        type: Boolean,
      },
      clearText: {
        type: String,
      },
      showExtraFilters: {
        type: Boolean,
        reflect: true,
        attribute: 'show-extra-filters',
      },
      _focused: {
        type: Boolean,
      },
    };
  }

  updated(changedProperties) {
    if (
      changedProperties.has('fieldValue') ||
      changedProperties.has('checkboxChecked') ||
      changedProperties.has('_focused')
    ) {
      this._debounceSearch(
        this.fieldValue,
        this.checkboxChecked,
        this._focused
      );
    }

    const notifyingProperties = [
      {
        property: 'fieldValue',
        eventName: 'field-value-changed',
      },
      {
        property: 'checkboxChecked',
        eventName: 'checkbox-checked-changed',
      },
    ];

    notifyingProperties.forEach(({ property, eventName }) => {
      if (changedProperties.has(property)) {
        this.dispatchEvent(
          new CustomEvent(eventName, {
            bubbles: true,
            composed: true,
            detail: {
              value: this[property],
            },
          })
        );
      }
    });
  }

  constructor() {
    super();
    this.buttonIcon = 'vaadin:plus';
    this.fieldIcon = 'vaadin:search';
    this.clearText = 'Clear search';
    this.showExtraFilters = false;
    this.showCheckbox = false;

    // In iOS prevent body scrolling to avoid going out of the viewport
    // when keyboard is opened
    this.addEventListener('touchmove', (e) => e.preventDefault());

    this._debounceSearch = debounce((fieldValue, checkboxChecked, focused) => {
      this.showExtraFilters = fieldValue || checkboxChecked || focused;
      // Set 1 millisecond wait to be able move from text field to checkbox with tab.
    }, 1);
  }

  _onFieldFocus(e) {
    if (e.currentTarget.id === 'field') {
      this.dispatchEvent(
        new Event('search-focus', { bubbles: true, composed: true })
      );
    }

    this._focused = true;
  }

  _onFieldBlur(e) {
    if (e.currentTarget.id === 'field') {
      this.dispatchEvent(
        new Event('search-blur', { bubbles: true, composed: true })
      );
    }

    this._focused = false;
  }
}

customElements.define(SearchBar.is, SearchBar);

function debounce(func, delay = 0) {
  let timeoutId;

  return (...args) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func(...args), delay);
  };
}
