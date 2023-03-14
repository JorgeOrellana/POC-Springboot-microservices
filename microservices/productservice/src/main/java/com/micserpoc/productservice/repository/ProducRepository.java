package com.micserpoc.productservice.repository;

import com.micserpoc.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProducRepository extends MongoRepository<Product, String>
{
}
