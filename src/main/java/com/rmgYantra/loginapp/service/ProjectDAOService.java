package com.rmgYantra.loginapp.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmgYantra.loginapp.model.Project;
import com.rmgYantra.loginapp.model.ProjectModule;
import com.rmgYantra.loginapp.model.ProjectStatus;
import com.rmgYantra.loginapp.repo.ProjectModuleRepo;
import com.rmgYantra.loginapp.repo.ProjectRepo;

@Service
public class ProjectDAOService {
	
	@Autowired
	private ProjectRepo projRepo;
	
	@Autowired
	private ProjectModuleRepo moduleRepo;
	
	public Project addProject(Project proj) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String dateCustom = format.format(date);
		proj.setCreatedOn(dateCustom);
		Project savedProject = projRepo.save(proj);
		return  savedProject;
	}
	
	public Page<Project> findAllProjects(Pageable pageable) {	
		List<Project> allVendors = projRepo.findAll();
        // Reverse the order of the list
        Collections.reverse(allVendors);
        // Create a sublist based on the specified Pageable
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allVendors.size());
        List<Project> pageContent = allVendors.subList(fromIndex, toIndex);
        // Create a Page object with the reversed and paginated data
        return new PageImpl<>(pageContent, pageable, allVendors.size());
	}
	
	public void deleteProject(String id) {
		projRepo.deleteById(id);
	}
	
	public boolean findProjectById(String projectId) {
		Project project = projRepo.findById(projectId).get();
		if(project!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	public ProjectModule addModule(ProjectModule module) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String customDate = format.format(date);
		module.setCreatedOn(customDate);
		ProjectModule savedModule = moduleRepo.save(module);
		return savedModule;
	}
	
	public ProjectStatus getProjectsStatusData() {
		ProjectStatus projectStatus = new ProjectStatus();
		List<Project> projects = projRepo.findAll();
		for(Project prj : projects) {
			try {
			if(prj.getStatus().equals("Created")) {
				projectStatus.setCreated(projectStatus.getCreated()+1);
			}
			else if(prj.getStatus().equals("On Going")) {
				projectStatus.setOnGoing(projectStatus.getOnGoing()+1);
			}else if(prj.getStatus().equals("Completed")) {
				projectStatus.setCompleted(projectStatus.getCompleted()+1);
			}
			}catch(Exception e) {
				projectStatus.setNa(projectStatus.getNa()+1);
			}
		}
		
		return projectStatus;
	}
}
