import { html, LitElement, css } from "lit-element";

import "../../../styles/shared-styles.js";

class AccessDeniedView extends LitElement {
  render() {
    return html` <h3 style="text-align: center">Access denied</h3> `;
  }

  static get is() {
    return "access-denied-view";
  }
}
customElements.define(AccessDeniedView.is, AccessDeniedView);
