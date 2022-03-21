import { css } from "lit";

// shared styles for all LitTemplate based views
export const sharedStyles = css`
  *,
  *::before,
  *::after,
  ::slotted(*) {
    box-sizing: border-box;
  }

  :host([hidden]),
  [hidden] {
    display: none !important;
  }

  h2,
  h3 {
    margin-top: var(--lumo-space-m);
    margin-bottom: var(--lumo-space-s);
  }

  h2 {
    font-size: var(--lumo-font-size-xxl);
  }

  h3 {
    font-size: var(--lumo-font-size-xl);
  }

  .scrollable {
    padding: var(--lumo-space-m);
    overflow: auto;
    -webkit-overflow-scrolling: touch;
  }

  .count {
    display: inline-block;
    background: var(--lumo-shade-10pct);
    border-radius: var(--lumo-border-radius);
    font-size: var(--lumo-font-size-s);
    padding: 0 var(--lumo-space-s);
    text-align: center;
  }

  .total {
    padding: 0 var(--lumo-space-s);
    font-size: var(--lumo-font-size-l);
    font-weight: bold;
    white-space: nowrap;
  }

  @media (min-width: 600px) {
    .total {
      min-width: 0;
      order: 0;
      padding: 0 var(--lumo-space-l);
    }
  }

  .flex {
    display: flex;
  }

  .flex1 {
    flex: 1 1 auto;
  }

  .bold {
    font-weight: 600;
  }
`;
