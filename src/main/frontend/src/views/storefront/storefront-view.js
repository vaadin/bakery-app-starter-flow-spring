import { html, css, LitElement } from 'lit';
import '@vaadin/grid';
import '@vaadin/dialog';
import '../../components/search-bar.js';
import './order-card.js';
import { sharedStyles } from '../../../styles/shared-styles.js';

class StorefrontView extends LitElement {
  static get styles() {
    return [
      sharedStyles,
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

      <vaadin-dialog id="dialog" theme="orders"></vaadin-dialog>
    `;
  }

  static get is() {
    return 'storefront-view';
  }

  ready() {
    super.ready();

    // This code is needed to measure the page load performance and can be safely removed
    // if there is no need for that.
    const grid = this.$.grid;
    const listener = () => {
      if (!grid.loading && window.performance.mark) {
        window.performance.mark('bakery-page-loaded');
        grid.removeEventListener('loading-changed', listener);
      }
    };
    grid.addEventListener('loading-changed', listener);
  }
}

customElements.define(StorefrontView.is, StorefrontView);
