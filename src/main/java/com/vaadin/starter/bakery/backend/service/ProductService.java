package com.vaadin.starter.bakery.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.repositories.ProductRepository;

@Service
public class ProductService implements FilterableCrudService<Product> {

	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> findAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.findByNameLikeIgnoreCase(repositoryFilter, null).getContent();
		} else {
			return find(null).getContent();
		}
	}

	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}


	public Page<Product> find(Pageable pageable) {
		return productRepository.findBy(pageable);
	}

	@Override
	public JpaRepository<Product, Long> getRepository() {
		return productRepository;
	}

	@Override
	public Product createNew() {
		return new Product();
	}
}
