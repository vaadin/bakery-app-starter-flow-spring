import { unsafeCSS } from "lit-element";
import { html, LitElement, css } from "lit-element";

import "@polymer/iron-icon/iron-icon.js";
import "@vaadin/vaadin-icons/vaadin-icons.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-checkbox/src/vaadin-checkbox.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "../../styles/shared-styles.js";

import { Debouncer } from "@polymer/polymer/lib/utils/debounce.js";
import { timeOut } from "@polymer/polymer/lib/utils/async.js";
class SearchBar extends LitElement {
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
      `,
    ];
  }
  render() {
    return html`
      <div class="row">
        <vaadin-text-field
          id="field"
          class="field"
          theme="white"
          .placeholder="${this.fieldPlaceholder}"
          .value="${this.fieldValue}"
          @value-changed="${(e) => (this.fieldValue = e.target.value)}"
          @focus="${this._onFieldFocus}"
          @blur="${this._onFieldBlur}"
        >
          <iron-icon slot="prefix" icon="${this.fieldIcon}"></iron-icon>
        </vaadin-text-field>
        <vaadin-checkbox
          class="checkbox desktop"
          .checked="${this.checkboxChecked}"
          @checked-changed="${(e) => (this.checkboxChecked = e.target.value)}"
          @focus="${this._onFieldFocus}"
          @blur="${this._onFieldBlur}"
          >${this.checkboxText}</vaadin-checkbox
        >
        <vaadin-button id="clear" class="clear-btn" theme="tertiary">
          ${this.clearText}
        </vaadin-button>
        <vaadin-button id="action" class="action-btn" theme="primary">
          <iron-icon slot="prefix" icon="${this.buttonIcon}"></iron-icon>
          ${this.buttonText}
        </vaadin-button>
      </div>

      <vaadin-checkbox
        class="checkbox mobile"
        .checked="${this.checkboxChecked}"
        @checked-changed="${(e) => (this.checkboxChecked = e.target.value)}"
        @focus="${this._onFieldFocus}"
        @blur="${this._onFieldBlur}"
        >${this.checkboxText}</vaadin-checkbox
      >
    `;
  }

  static get is() {
    return "search-bar";
  }
  static get properties() {
    return {
      fieldPlaceholder: {
        type: String,
      },
      fieldValue: {
        type: String,
        notify: true,
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
      },
      checkboxText: {
        type: String,
      },
      checkboxChecked: {
        type: Boolean,
        notify: true,
      },
      clearText: {
        type: String,
      },
      showExtraFilters: {
        type: Boolean,

        reflect: true,
      },
      _isSafari: {
        type: Boolean,
      },
    };
  }

  static get observers() {
    return ["_setShowExtraFilters(fieldValue, checkboxChecked, _focused)"];
  }

  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);
    // In iOS prevent body scrolling to avoid going out of the viewport
    // when keyboard is opened
    this.addEventListener("touchmove", (e) => e.preventDefault());
  }

  _setShowExtraFilters(fieldValue, checkboxChecked, focused) {
    this._debouncer = Debouncer.debounce(
      this._debouncer,
      // Set 1 millisecond wait to be able move from text field to checkbox with tab.
      timeOut.after(1),
      () => {
        this.showExtraFilters = fieldValue || checkboxChecked || focused;

        // Safari has an issue with repainting shadow root element styles when a host attribute changes.
        // Need this workaround (toggle any inline css property on and off) until the issue gets fixed.
        // Issue is fixed in Safari 11 Tech Preview version.
        if (this._isSafari && this.root) {
          Array.from(this.root.querySelectorAll("*")).forEach(function (el) {
            el.style["-webkit-backface-visibility"] = "visible";
            el.style["-webkit-backface-visibility"] = "";
          });
        }
      }
    );
  }

  _onFieldFocus(e) {
    e.target == this.renderRoot.querySelector("#field") &&
      this.dispatchEvent(
        new Event("search-focus", { bubbles: true, composed: true })
      );
    this._focused = true;
  }

  _onFieldBlur(e) {
    e.target == this.renderRoot.querySelector("#field") &&
      this.dispatchEvent(
        new Event("search-blur", { bubbles: true, composed: true })
      );
    this._focused = false;
  }
  constructor() {
    super();
    this.fieldIcon = "vaadin:search";
    this.buttonIcon = "vaadin:plus";
    this.showCheckbox = false;
    this.clearText = "Clear search";
    this.showExtraFilters = false;
    this._isSafari = /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
  }
}

window.customElements.define(SearchBar.is, SearchBar);
