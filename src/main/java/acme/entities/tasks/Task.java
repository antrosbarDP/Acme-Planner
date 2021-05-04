package acme.entities.tasks;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends DomainEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------


	@NotBlank
	@Size(max = 80)
	protected String		title;
	
	@NotBlank
	@Size(max = 500)
	protected String		description;
	
	@URL
	protected String		link;
	
	protected Boolean 	isPublic;
	
	protected Boolean 	isFinished;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date			executionPeriodStart;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date			executionPeriodEnd;
	
	@NotNull
	protected Integer		workloadHours;
	
	
	@Range(max = 60, min = 0 )
	protected Integer		workloadMinutes;
	

	// Derived attributes -----------------------------------------------------
	
	protected Duration workload;
	
	protected Long executionPeriod;
	
	public Long getWorkload() {
		Long result;
		
		final Integer minutes = this.getWorkloadMinutes();
		final Integer hours = this.getWorkloadHours();
		final Duration duration = Duration.ofHours(hours);
		if (minutes != null) {
			result = duration.plusMinutes(minutes).toHours();
		}else {
			result = duration.toHours();
		}
		
		

		return result;
	}
	
	
	public Long getExecutionPeriod() {
		Long result;
		
		final long diff = this.getExecutionPeriodEnd().getTime() - this.getExecutionPeriodStart().getTime();

		result = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		
		

		return result;
	}
	

		
		
	// Relationships ----------------------------------------------------------

}
