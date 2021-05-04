/*
 * AdministratorDashboardRepository.java
 *
 * Copyright (C) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tasks.Task;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select t from Task t")
	Collection<Task> findMany();
	
	@Query("select t from Task t where t.id = ?1")
	Task findOneById(int id);
	
	@Query("select t from Task t where (t.isPublic = TRUE)")
	Collection<Task> findManyPublic();
	
	@Query("select t from Task t where (t.isPublic = FALSE)")
	Collection<Task> findManyPrivate();
	
	@Query("select t from Task t where (t.isFinished = FALSE)")
	Collection<Task> findManyUnfinished();
	
	@Query("select t from Task t where (t.isFinished = TRUE)")
	Collection<Task> findManyFinished();

}
