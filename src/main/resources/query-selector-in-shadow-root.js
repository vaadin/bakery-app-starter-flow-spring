function queryShadowSelectorAll(selector, context) {
  context = context || document;
  const index = selector.indexOf('::shadow');
  if (index > -1) {
    const hostSelector = selector.substring(0, index);
    const rest = selector.substring(index + '::shadow'.length);
    const host = hostSelector.trim() === '' ? context : context.querySelector(hostSelector);
    return queryShadowSelectorAll(rest, host.shadowRoot);
  } else {
    return context.querySelectorAll(selector);
  }
}

// Selenium injects the contents of this file into an anonymous function
// eslint-disable-next-line
return queryShadowSelectorAll(arguments[0], arguments[1]);
