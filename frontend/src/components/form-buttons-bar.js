import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import './buttons-bar.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
{
  class FormButtonsBarElement extends PolymerElement {
    static get template() {
      return html`
<buttons-bar>
 <vaadin-button id="save" slot="left" theme="raised primary">
   Save
 </vaadin-button>
 <vaadin-button id="cancel" slot="left" theme="raised">
   Cancel
 </vaadin-button>
 <vaadin-button id="delete" slot="right" theme="raised tertiary error">
   Delete
 </vaadin-button>
</buttons-bar>
`;
    }

    static get is() {
      return 'form-buttons-bar';
    }
  }
  window.customElements.define(FormButtonsBarElement.is, FormButtonsBarElement);
}
