import { html, css, LitElement } from 'lit';
import '@vaadin/board';
import '@vaadin/board/vaadin-board-row.js';
import '@vaadin/charts';
import '@vaadin/grid';
import '../storefront/order-card.js';
import './dashboard-counter-label.js';
import { sharedStyles } from '../../../styles/shared-styles.js';

class DashboardView extends LitElement {
  static get styles() {
    return [
      sharedStyles,
      css`
        :host {
          width: 100%;
          -webkit-overflow-scrolling: touch;
          overflow: auto;
        }

        .vaadin-board-cell {
          padding: var(--lumo-space-s);
        }

        *::-ms-backdrop,
        .vaadin-board-cell {
          padding: 0;
        }

        .column-chart {
          box-shadow: 0 2px 5px 0 rgba(23, 68, 128, 0.1);
          border-radius: 4px;
          height: calc(20vh - 64px) !important;
          min-height: 150px;
        }

        #yearlySalesGraph {
          height: calc(30vh - 64px) !important;
          min-height: 200px;
        }

        #monthlyProductSplit,
        #ordersGrid {
          border-radius: 4px;
          box-shadow: 0 2px 5px 0 rgba(23, 68, 128, 0.1);
          height: calc(40vh - 64px) !important;
          min-height: 355px;
        }

        vaadin-board-row.custom-board-row {
          --vaadin-board-width-medium: 1440px;
          --vaadin-board-width-small: 1024px;
        }
      `,
    ];
  }

  render() {
    return html`
      <vaadin-board>
        <vaadin-board-row>
          <dashboard-counter-label id="todayCount" class="green">
            <vaadin-chart
              id="todayCountChart"
              class="counter"
              theme="classic"
            ></vaadin-chart>
          </dashboard-counter-label>
          <dashboard-counter-label
            id="notAvailableCount"
            class="red"
          ></dashboard-counter-label>
          <dashboard-counter-label
            id="newCount"
            class="blue"
          ></dashboard-counter-label>
          <dashboard-counter-label
            id="tomorrowCount"
            class="gray"
          ></dashboard-counter-label>
        </vaadin-board-row>
        <vaadin-board-row>
          <div class="vaadin-board-cell">
            <vaadin-chart
              id="deliveriesThisMonth"
              class="column-chart"
              theme="classic"
            ></vaadin-chart>
          </div>
          <div class="vaadin-board-cell">
            <vaadin-chart
              id="deliveriesThisYear"
              class="column-chart"
              theme="classic"
            ></vaadin-chart>
          </div>
        </vaadin-board-row>
        <vaadin-board-row>
          <vaadin-chart
            id="yearlySalesGraph"
            class="yearly-sales"
            theme="classic"
          ></vaadin-chart>
        </vaadin-board-row>
        <vaadin-board-row class="custom-board-row">
          <div class="vaadin-board-cell">
            <vaadin-chart
              id="monthlyProductSplit"
              class="product-split-donut"
              theme="classic"
            ></vaadin-chart>
          </div>
          <div class="vaadin-board-cell">
            <vaadin-grid id="ordersGrid" theme="orders dashboard"></vaadin-grid>
          </div>
        </vaadin-board-row>
      </vaadin-board>
    `;
  }

  static get is() {
    return 'dashboard-view';
  }

  // This method is overridden to measure the page load performance and can be safely removed
  // if there is no need for that.
  firstUpdated() {
    super.firstUpdated();
    this._chartsLoaded = new Promise((resolve, reject) => {
      // save the 'resolve' callback to trigger it later from the server
      this._chartsLoadedResolve = () => {
        resolve();
      };
    });

    this._gridLoaded = new Promise((resolve, reject) => {
      const ordersGrid = this.shadowRoot.querySelector('#ordersGrid');
      const listener = () => {
        if (!ordersGrid.loading) {
          ordersGrid.removeEventListener('loading-changed', listener);
          resolve();
        }
      };
      ordersGrid.addEventListener('loading-changed', listener);
    });

    Promise.all([this._chartsLoaded, this._gridLoaded]).then(() => {
      window.performance.mark && window.performance.mark('bakery-page-loaded');
    });
  }
}

customElements.define(DashboardView.is, DashboardView);
