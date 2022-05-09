package com.tweetapp.model;

import java.util.Date;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
public abstract class AudtitingDetails {
	@LastModifiedDate
    private Date lastModifiedDate;
}
