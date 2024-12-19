package com.rmgYantra.loginapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
public class Feedback {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "feedback_seq")
	@GenericGenerator(name = "feedback_seq",strategy = "com.rmgYantra.loginapp.model.StringPrefixedSequesceGenerator",
	parameters = {@Parameter(name = StringPrefixedSequesceGenerator.VALUE_PREFIX_PARAMETER, value = "UF_"),
			@Parameter(name = StringPrefixedSequesceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
			@Parameter(name = StringPrefixedSequesceGenerator.INCREMENT_PARAM, value = "1")})
	@ApiModelProperty(notes = "Employee ID is Auto-generated By the Server,don't ask from user",hidden = true)
	private String feedbackId;
	private String name;
	private String email;
	private String feedback;
}
