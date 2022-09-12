import { unsafeCSS } from "lit-element";
import { html, LitElement, css } from "lit-element";

import "@vaadin/vaadin-board/vaadin-board.js";
import "@vaadin/vaadin-board/vaadin-board-row.js";
import "@vaadin/vaadin-charts/vaadin-chart.js";
import "@vaadin/vaadin-grid/src/vaadin-grid.js";
import "../../../styles/shared-styles.js";
import "../../../styles/bakery-charts-theme.js";
import "../storefront/order-card.js";
import "./dashboard-counter-label.js";

class DashboardView extends LitElement {
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
            <vaadin-chart id="todayCountChart" class="counter"></vaadin-chart>
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
            ></vaadin-chart>
          </div>
          <div class="vaadin-board-cell">
            <vaadin-chart
              id="deliveriesThisYear"
              class="column-chart"
            ></vaadin-chart>
          </div>
        </vaadin-board-row>
        <vaadin-board-row>
          <vaadin-chart
            id="yearlySalesGraph"
            class="yearly-sales"
          ></vaadin-chart>
        </vaadin-board-row>
        <vaadin-board-row class="custom-board-row">
          <div class="vaadin-board-cell">
            <vaadin-chart
              id="monthlyProductSplit"
              class="product-split-donut"
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
    return "dashboard-view";
  }

  // This method is overridden to measure the page load performance and can be safely removed
  // if there is no need for that.
  firstUpdated(_changedProperties) {
    super.firstUpdated(_changedProperties);
    this._chartsLoaded = new Promise((resolve, reject) => {
      // save the 'resolve' callback to trigger it later from the server
      this._chartsLoadedResolve = () => {
        resolve();
      };
    });

    this._gridLoaded = new Promise((resolve, reject) => {
      const listener = () => {
        if (!this.renderRoot.querySelector("#ordersGrid']").loading) {
          this.renderRoot
            .querySelector("#ordersGrid']")
            .removeEventListener("loading-changed", listener);
          resolve();
        }
      };
      this.renderRoot
        .querySelector("#ordersGrid']")
        .addEventListener("loading-changed", listener);
    });

    Promise.all([this._chartsLoaded, this._gridLoaded]).then(() => {
      window.performance.mark && window.performance.mark("bakery-page-loaded");
    });
  }
}

window.customElements.define(DashboardView.is, DashboardView);
