package com.vaadin.starter.bakery.ui.view.admin.products;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.DECIMAL_ZERO;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValueContext;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.view.admin.EditForm;
import com.vaadin.starter.bakery.ui.view.admin.EntityEditor;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.Span;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

@Tag("product-edit")
@HtmlImport("src/products/product-edit.html")
public class ProductEdit extends PolymerTemplate<TemplateModel> implements EntityEditor<Product> {

	@Id("product-edit-name")
	private TextField nameField;

	@Id("price")
	private TextField priceField;

	@Id("product-edit-form")
	private EditForm editForm;

	private final BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

	private final DecimalFormat df = FormattingUtils.getUiPriceFormatter();

	public ProductEdit() {
		editForm.init(binder, "Product");
		binder.bind(nameField, "name");
		binder.forField(priceField).withConverter(new PriceConverter()).bind("price");
		priceField.addToPrefix(new Span(Currency.getInstance(Locale.getDefault()).getSymbol()));

		// Forward these events to the presenter
		editForm.addListener(SaveEvent.class, this::fireEvent);
		editForm.addListener(DeleteEvent.class, this::fireEvent);
		editForm.addListener(CancelEvent.class, this::fireEvent);
	}

	public void read(Product product) {
		binder.readBean(product);
		editForm.showEditor(product.isNew());
	}

	public void write(Product product) throws ValidationException {
		binder.writeBean(product);
	}

	@EventHandler
	public void priceFocusGained() {
		if (DECIMAL_ZERO.equals(priceField.getValue())) {
			priceField.setValue("");
		}
	}

	public boolean isDirty() {
		return binder.hasChanges();
	}

	class PriceConverter implements Converter<String, Integer> {

		@Override
		public Result<Integer> convertToModel(String presentationValue, ValueContext valueContext) {
			try {
				if (presentationValue == null || presentationValue.isEmpty()) {
					return Result.error("Price is required");
				} else {
					return Result.ok((int) Math.round(df.parse(presentationValue).doubleValue() * 100));
				}
			} catch (ParseException e) {
				return Result.error("Invalid value");
			}
		}

		@Override
		public String convertToPresentation(Integer modelValue, ValueContext valueContext) {
			return convertIfNotNull(modelValue, i -> df.format(BigDecimal.valueOf(i, 2)));
		}
	}
}
