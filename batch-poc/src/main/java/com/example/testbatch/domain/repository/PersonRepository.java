package com.example.testbatch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testbatch.domain.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person,Integer> {
}
