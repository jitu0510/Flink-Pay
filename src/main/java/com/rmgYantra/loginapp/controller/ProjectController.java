package com.rmgYantra.loginapp.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.rmgYantra.loginapp.exceptions.ModuleNameAlreadyPresentException;
import com.rmgYantra.loginapp.exceptions.ProjectNameAlreadyPresentException;
import com.rmgYantra.loginapp.exceptions.ResourceNotFoundException;
import com.rmgYantra.loginapp.model.Employee;
import com.rmgYantra.loginapp.model.Project;
import com.rmgYantra.loginapp.model.ProjectModule;
import com.rmgYantra.loginapp.model.ProjectStatus;
import com.rmgYantra.loginapp.repo.EmployeeRepo;
import com.rmgYantra.loginapp.repo.ProjectModuleRepo;
import com.rmgYantra.loginapp.repo.ProjectRepo;
import com.rmgYantra.loginapp.service.ProjectDAOService;

@RestController
@CrossOrigin(origins = {"*","http://localhost:4200"})
public class ProjectController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	public ProjectDAOService projDAOService;

	@Autowired
	public ProjectRepo projRepo;
	
	@Autowired
	private ProjectModuleRepo moduleRepo;
	
	@Autowired
	private EmployeeRepo employeeRepo;

	@PostMapping("/add")
	public void addProject(@RequestBody String encryptedData, @RequestHeader("key") String key) {
		System.out.println(encryptedData);
		System.out.println(key);	
	}
	
	@PostMapping("/addProject")
	public ResponseEntity addProject(@RequestBody Project project) {
		
		if(projRepo.findByProjectName(project.getProjectName()).isPresent())
		{
			throw new ProjectNameAlreadyPresentException("The Project Name :"+project.getProjectName()+" Already Exists");
		}
		Project proj = projDAOService.addProject(project);
		HashMap<String, String> hm = new HashMap<>();
		hm.put("projectName", proj.getProjectName());
		hm.put("projectId", proj.getProjectId());
		hm.put("createdOn", proj.getCreatedOn());
		hm.put("status",proj.getStatus()); 
		hm.put("createdBy", proj.getCreatedBy());
		hm.put("msg", "Successfully Added");
		ResponseEntity re = new ResponseEntity(hm, HttpStatus.CREATED);
		return re;
	}

	@GetMapping("/projects")
	public List<Project> getAllProjects() {
		logger.info("/projects called");
		List<Project> projects = projRepo.findAll();
		return projects;
	}
	@GetMapping("/projects1")
	public Page<Project> getAllProjects(Pageable pageable) {
		Page<Project> projects = projDAOService.findAllProjects(pageable);
        return projects;
	}

	@GetMapping("/count-projects")
	public Long getProjectsCounts() {
		return projRepo.count();
	}
	
	@GetMapping("/projects/{projectId}")
	public Project getSingleProject(@PathVariable String projectId) {
		return projRepo.findById(projectId).get();
	}

	@PutMapping("/projects/{projectId}")
	public ResponseEntity<Project> updateProject(@PathVariable String projectId, @RequestBody Project project) {
		Project proj = projRepo.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project Not Found For the Id::" + projectId));
		proj.setProjectId(projectId);
		proj.setCreatedBy(project.getCreatedBy());
		proj.setProjectName(project.getProjectName());
		proj.setTeamSize(project.getTeamSize());
		proj.setStatus(project.getStatus());
		Project updatedProject = projRepo.save(proj);
		return ResponseEntity.ok(updatedProject);
	}

	@DeleteMapping("/projects/{projectId}")
	public ResponseEntity deleteProject(@PathVariable String projectId) {
		   Project project = projRepo.findByProjectId(projectId);
		   List<Employee> employees = employeeRepo.findByProject(projectId);
		if (project!=null && employees.size()==0) {
			projDAOService.deleteProject(projectId);
			HashMap<String, String> hm = new HashMap<>();
			hm.put("msg", "resource deleted successfully");
			ResponseEntity re = new ResponseEntity(hm, HttpStatus.NO_CONTENT);
			return re;
		} else {
			throw new ResourceNotFoundException("Project Not Found For the Id::" + projectId);
		}
	}
	
	@PostMapping("/addmodule")
	public ResponseEntity<?> addModule(@RequestBody ProjectModule module){
		if(moduleRepo.findByModuleName(module.getModuleName()).isPresent())
		{
			throw new ModuleNameAlreadyPresentException("The Module Name :"+module.getModuleName()+" Already Exists");
		}
		
		ProjectModule projectModule = projDAOService.addModule(module);
		HashMap<String, String> hm = new HashMap<>();
		hm.put("moduleName", projectModule.getModuleName());
		hm.put("moduleId", projectModule.getModuleId());
		hm.put("createdOn", projectModule.getCreatedOn());
		hm.put("status",projectModule.getStatus()); 
		hm.put("assignedTo", projectModule.getAssignedTo());
		hm.put("assignedOn", projectModule.getAssignedOn());
		hm.put("msg", "Successfully Added");
		ResponseEntity<?> responseEntity = new ResponseEntity(hm, HttpStatus.CREATED);
		return responseEntity;
	}
	
	@GetMapping("/all-modules/{projectid}")
	public Page<ProjectModule> getAllModules(@PathVariable("projectid") String projectid,Pageable pageable) {
		Page<ProjectModule> modules = moduleRepo.findAll(pageable);
		return modules;
	}
	
	@GetMapping("/project-status-data")
	public ResponseEntity<?> getProjectsStatusData(){
		ProjectStatus projectStatus = projDAOService.getProjectsStatusData();
		if(projectStatus != null) {
			return new ResponseEntity(projectStatus,HttpStatus.OK);
		}else {
			return new ResponseEntity(projectStatus,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
}
