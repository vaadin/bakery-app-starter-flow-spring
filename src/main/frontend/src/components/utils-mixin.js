/**
 * Adds the scroll-shadow attribute if there is a `#main` element with scrollsize
 * larger than its height, and there is hidden content at the bottom.
 */
export const ScrollShadowMixin = (subclass) =>
  class extends subclass {
    static get properties() {
      return {
        noScroll: {
          type: Boolean,
          reflect: true,
          attribute: 'no-scroll',
        },
        _main: {
          attribute: false,
        },
      };
    }

    firstUpdated() {
      super.firstUpdated();

      this._main = this.shadowRoot.querySelector('#main');

      if (this._main) {
        this._main.addEventListener('scroll', () => this._contentScroll());
        this._contentScroll();
      }
    }

    _contentScroll() {
      if (this._main) {
        this.noScroll =
          this._main.scrollHeight - this._main.scrollTop ==
          this._main.clientHeight;
      }
    }
  };
