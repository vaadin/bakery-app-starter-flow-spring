package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.service.PickupLocationService;

/**
 * A singleton data provider which knows which pickup locations are available.
 */
@SpringComponent
public class PickupLocationDataProvider extends AbstractBackEndDataProvider<String, String> {

	private transient PickupLocationService pickupLocationService;

	public PickupLocationDataProvider(PickupLocationService pickupLocationService) {
		this.pickupLocationService = pickupLocationService;
	}

	@Override
	protected int sizeInBackEnd(Query<String, String> query) {
		return (int) pickupLocationService.countAnyMatching(query.getFilter());
	}

	@Override
	public Stream<String> fetchFromBackEnd(Query<String, String> query) {
		return findLocations(query).stream().map(p -> p.getName());
	}

	public List<PickupLocation> findLocations(Query<String, String> query) {
		return pickupLocationService
				.findAnyMatching(query.getFilter(), new PageRequest(query.getOffset(), query.getLimit())).getContent();

	}
}
