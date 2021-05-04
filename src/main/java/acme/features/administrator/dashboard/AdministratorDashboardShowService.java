/*
 * AdministratorDashboardShowService.java
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
import java.util.stream.DoubleStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tasks.Task;
import acme.forms.Dashboard;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractShowService;

@Service
public class AdministratorDashboardShowService implements AbstractShowService<Administrator, Dashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorDashboardRepository repository;

	// AbstractShowService<Administrator, Dashboard> interface ----------------


	@Override
	public boolean authorise(final Request<Dashboard> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Dashboard> request, final Dashboard entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, //
									"totalPublicTasks",
								"totalPrivateTasks",
								"totalFinishedTasks",
								"totalUnfinishedTasks",
								"averageExecutionPeriod",
								"averageWorkload",
								"maximumExecutionPeriod",
								"minimumExecutionPeriod",
								"maximumWorkload",
								"minimumWorkload",
								"standardDeviationWorkload",
							"standardDeviationExecutionPeriod");
	}

	@Override
	public Dashboard findOne(final Request<Dashboard> request) {
		assert request != null;

		Dashboard result;
		final Double						totalPublicTasks;
		final Double						totalPrivateTasks;
		final Double						totalFinishedTasks;
		final Double						totalUnfinishedTasks;
		final Double						averageExecutionPeriod;
		final Double						averageWorkload;
		final Double						maximumExecutionPeriod;
		final Double						minimumExecutionPeriod;
		final Double						maximumWorkload;
		final Double						minimumWorkload;
		final Double						standardDeviationWorkload;
		final Double						standardDeviationExecutionPeriod;

		final Collection<Task> tasks =  this.repository.findMany();
		totalPublicTasks = (double) this.repository.findManyPublic().size();
		totalPrivateTasks = (double) this.repository.findManyPrivate().size();
		totalFinishedTasks = (double) this.repository.findManyFinished().size();
		totalUnfinishedTasks = (double) this.repository.findManyUnfinished().size();
		averageExecutionPeriod = tasks.stream().mapToDouble(t->t.getExecutionPeriod()).average().getAsDouble();
		maximumExecutionPeriod = tasks.stream().mapToDouble(t->t.getExecutionPeriod()).max().getAsDouble();
		minimumExecutionPeriod = tasks.stream().mapToDouble(t->t.getExecutionPeriod()).min().getAsDouble();
		standardDeviationExecutionPeriod = this.standardDeviation(tasks.size(), averageExecutionPeriod, tasks.stream().mapToDouble(t->t.getExecutionPeriod()));
		averageWorkload = tasks.stream().mapToDouble(t->t.getWorkload()).average().getAsDouble();
		maximumWorkload = tasks.stream().mapToDouble(t->t.getWorkload()).max().getAsDouble();
		minimumWorkload = tasks.stream().mapToDouble(t->t.getWorkload()).min().getAsDouble();
		standardDeviationWorkload = this.standardDeviation(tasks.size(), averageWorkload, tasks.stream().mapToDouble(t->t.getWorkload()));
		

		result = new Dashboard();
		result.setAverageExecutionPeriod(averageExecutionPeriod);
		result.setAverageWorkload(averageWorkload);
		result.setMaximumExecutionPeriod(maximumExecutionPeriod);
		result.setMaximumWorkload(maximumWorkload);
		result.setMinimumExecutionPeriod(minimumExecutionPeriod);
		result.setMinimumWorkload(minimumWorkload);
		result.setStandardDeviationExecutionPeriod(standardDeviationExecutionPeriod);
		result.setStandardDeviationWorkload(standardDeviationWorkload);
		result.setTotalFinishedTasks(totalFinishedTasks);
		result.setTotalPrivateTasks(totalPrivateTasks);
		result.setTotalUnfinishedTasks(totalUnfinishedTasks);
		result.setTotalPublicTasks(totalPublicTasks);

		return result;
	}
	
	private Double standardDeviation(final Integer total, final Double average,  final DoubleStream elements) {
		
		final Double sum = elements.map(e ->  Math.pow(e - average,2)).sum();
		final Double result = Math.sqrt(sum/total);
		
		return result;
	}

}
