package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.service.PickupLocationService;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Stream;

/**
 * A singleton data provider which knows which pickup locations are available.
 */
@SpringComponent
public class PickupLocationComboBoxDataProvider extends AbstractDataProvider<String, String> {

	private transient PickupLocationService pickupLocationService;

	public PickupLocationComboBoxDataProvider(PickupLocationService pickupLocationService) {
		this.pickupLocationService = pickupLocationService;
	}

	@Override
	public boolean isInMemory() {
		return false;
	}

	@Override
	public int size(Query<String, String> query) {
		return (int) pickupLocationService.countAnyMatching(query.getFilter());
	}

	@Override
	public Stream<String> fetch(Query<String, String> query) {
		return findLocations(query).stream().map(p -> p.getName());
	}

	public List<PickupLocation> findLocations(Query<String, String> query) {
		return pickupLocationService
				.findAnyMatching(query.getFilter(), new PageRequest(query.getOffset(), query.getLimit())).getContent();

	}
}

