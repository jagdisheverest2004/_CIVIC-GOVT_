package org.example.civic_govt.service;

import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.Zone;
import org.example.civic_govt.payload.issues.CreateIssueDTO;
import org.example.civic_govt.payload.issues.FetchIssueDTO;
import org.example.civic_govt.payload.issues.FetchIssuesDTO;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.example.civic_govt.repository.DepartmentRepository;
import org.example.civic_govt.repository.IssueRepository;
import org.example.civic_govt.repository.UserRepository;
import org.example.civic_govt.util.IssueSpecification;
import org.example.civic_govt.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private IssueSpecification issueSpecification;

    @Autowired
    private PageUtil pageUtil;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    private String constructImageUrl(String fileName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + fileName : imageBaseUrl + "/" + fileName;
    }

    public String uploadImage(String path, MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs(); // Create the directory if it does not exist
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    public FetchIssueDTO createFetchIssueDTO(Issue issue) {
        FetchIssueDTO dto = new FetchIssueDTO();
        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setLatitude(issue.getLatitude());
        dto.setLongitude(issue.getLongitude());
        dto.setStatus(issue.getStatus().name());
        dto.setPriority(issue.getPriority().name());
        dto.setDepartmentName(issue.getDepartment().getName());
        dto.setDistrictName(issue.getDistrict().getName());
        dto.setZoneName(issue.getZone().getName());
        dto.setReporters(issue.getReporters().stream().map(User::getUsername).toList());
        dto.setCreatedAt(issue.getCreatedAt());
        dto.setUpdatedAt(issue.getUpdatedAt());
        dto.setUpvoteCount((long) issue.getVotes().size());
        dto.setCommentCount((long) issue.getComments().size());
        return dto;
    }

    public FetchIssueDTO createIssue(CreateIssueDTO createIssueDTO, User reporter, MultipartFile[] images) {
        // Find existing issues based on location and category
        Optional<Issue> existingIssue = issueRepository.findByTitleAndLatitudeAndLongitude(
                createIssueDTO.getTitle(),
                createIssueDTO.getLatitude(),
                createIssueDTO.getLongitude()
        );

        if (existingIssue.isPresent()) {
            // If the issue exists, add the new reporter as a contributor
            Issue foundIssue = existingIssue.get();
            // Handle image uploads if any
            if (images != null) {
                for (MultipartFile image : images) {
                    try {
                        String fileName = uploadImage(path, image);
                        String imageUrl = constructImageUrl(fileName);
                        if (foundIssue.getPhotos() != null) {
                            foundIssue.getPhotos().add(imageUrl);
                        } else {
                            foundIssue.setPhotos(List.of(imageUrl));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!foundIssue.getReporters().contains(reporter)) {
                foundIssue.getReporters().add(reporter);
                issueRepository.save(foundIssue);
                notificationService.createNotification(reporter, "You have successfully reported the issue '" + createIssueDTO.getTitle() + "'.");
            }
            else {
                notificationService.createNotification(reporter, "You have reported the same issue '" + createIssueDTO.getTitle() + "' previously.");
            }
            return createFetchIssueDTO(foundIssue);

        } else {
            // New issue, so create a new Issue object from the DTO
            Issue newIssue = new Issue();
            newIssue.setTitle(createIssueDTO.getTitle());
            newIssue.setLatitude(createIssueDTO.getLatitude());
            newIssue.setLongitude(createIssueDTO.getLongitude());

            // Set default values and add the first contributor
            newIssue.setStatus(Issue.Status.PENDING);
            newIssue.setPriority(Issue.Priority.valueOf(createIssueDTO.getPriority()));
            newIssue.setReporters(List.of(reporter));
            newIssue.setCreatedAt(LocalDateTime.now());
            newIssue.setUpdatedAt(LocalDateTime.now());

            // Get department and other hierarchical data
            Department department = departmentRepository.findByName(createIssueDTO.getDepartmentName())
                    .orElseThrow(() -> new RuntimeException("Department not found."));
            newIssue.setDepartment(department);

            // Logic to handle custom issues
            if (!department.getDefaultIssueTypes().contains(newIssue.getTitle())) {
                department.getDefaultIssueTypes().add(newIssue.getTitle());
                departmentRepository.save(department);
            }
            issueRepository.save(newIssue);
            notificationService.createNotification(reporter, "You have successfully reported the issue '" + createIssueDTO.getTitle() + "'.");
            return createFetchIssueDTO(newIssue);
        }
    }

    public void updateIssueStatus(Long issueId, Issue.Status newStatus, User subordinate) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found with id " + issueId));
        issue.setStatus(newStatus);
        issue.setUpdatedAt(LocalDateTime.now());
        issueRepository.save(issue);
        List<User> reporters = issue.getReporters();
        for(User reporter:reporters){
            notificationService.createNotification(reporter, "The status of your reported issue '" + issue.getTitle() + "' has been updated to " + newStatus.name() + " by " + subordinate.getUsername() + ".");
        }
    }

    public void assignIssue(Long assigneeId, Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found with id " + issueId));
        Zone zone = issueRepository.findZoneById(issueId).orElseThrow(() -> new RuntimeException("Zone not found with issue id " + issueId));
        Boolean isInZone = userRepository.isUserInZone(assigneeId, zone.getId());
        if(!isInZone) {
            throw new RuntimeException("User with id " + assigneeId + " is not in the zone of the issue with id " + issueId);
        }
        if(issue.getAssignee() != null) {
            throw new RuntimeException("Issue with id " + issueId + " is already assigned to user with id " + issue.getAssignee().getId());
        }
        User assignee = userRepository.findById(assigneeId).orElseThrow(() -> new RuntimeException("User not found with id " + assigneeId));
        assignee.getAssignedIssues().add(issue);
        issue.setAssignee(assignee);
        userRepository.save(assignee);
        issueRepository.save(issue);
        notificationService.createNotification(assignee, "You have been assigned to the issue '" + issue.getTitle() + "'");
        List<User> reporters = issue.getReporters();
        for(User reporter:reporters){
            notificationService.createNotification(reporter, "Your issue '" + issue.getTitle() +" have been assigned to "+ assignee.getUsername() + ".");
        }
    }

    public Optional<Issue> findById(Long issueId) {
        return issueRepository.findById(issueId);
    }

    public FetchIssuesDTO getAllIssues(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Issue> issuePage = issueRepository.findAll(pageable);
        return getFetchIssuesDTO(issuePage);
    }

    private FetchIssuesDTO getFetchIssuesDTO(Page<Issue> issuePage) {
        List<FetchIssueDTO> issueDTOs = issuePage.getContent().stream().map(this::createFetchIssueDTO).toList();
        FetchIssuesDTO fetchIssuesDTO = new FetchIssuesDTO();
        fetchIssuesDTO.setIssuesDTO(issueDTOs);
        fetchIssuesDTO.setPageNumber(issuePage.getNumber());
        fetchIssuesDTO.setPageSize(issuePage.getSize());
        fetchIssuesDTO.setTotalElements(issuePage.getTotalElements());
        fetchIssuesDTO.setTotalPages(issuePage.getTotalPages());
        fetchIssuesDTO.setLastPage(issuePage.isLast());
        return fetchIssuesDTO;
    }

    public FetchIssuesDTO getIssuesWithFilters(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, IssueFilterDTO filters) {
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Specification<Issue> spec = IssueSpecification.withFilters(filters);
        Page<Issue> issuePage = issueRepository.findAll(spec, pageable);
        return getFetchIssuesDTO(issuePage);
    }

    public FetchIssuesDTO getReportedIssues(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, IssueFilterDTO filters) {
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Specification<Issue> spec = (IssueSpecification.byReporter(userId));
        spec = spec.and(IssueSpecification.withFilters(filters));
        Page<Issue> issuePage = issueRepository.findAll(spec, pageable);
        List<FetchIssueDTO> issueDTOs = issuePage.getContent().stream().map(this::createFetchIssueDTO).toList();
        FetchIssuesDTO fetchIssuesDTO = new FetchIssuesDTO();
        fetchIssuesDTO.setIssuesDTO(issueDTOs);
        fetchIssuesDTO.setPageNumber(issuePage.getNumber());
        fetchIssuesDTO.setPageSize(issuePage.getSize());
        fetchIssuesDTO.setTotalElements(issuePage.getTotalElements());
        fetchIssuesDTO.setTotalPages(issuePage.getTotalPages());
        fetchIssuesDTO.setLastPage(issuePage.isLast());
        return fetchIssuesDTO;
    }

    public FetchIssuesDTO getAssignedIssues(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, IssueFilterDTO filters) {
        Pageable pageable = pageUtil.createPageable(pageNumber, pageSize, sortBy, sortOrder);
        Specification<Issue> spec = (IssueSpecification.byAssignee(userId));
        spec = spec.and(IssueSpecification.withFilters(filters));
        Page<Issue> issuePage = issueRepository.findAll(spec, pageable);
        List<FetchIssueDTO> issueDTOs = issuePage.getContent().stream().map(this::createFetchIssueDTO).toList();
        FetchIssuesDTO fetchIssuesDTO = new FetchIssuesDTO();
        fetchIssuesDTO.setIssuesDTO(issueDTOs);
        fetchIssuesDTO.setPageNumber(issuePage.getNumber());
        fetchIssuesDTO.setPageSize(issuePage.getSize());
        fetchIssuesDTO.setTotalElements(issuePage.getTotalElements());
        fetchIssuesDTO.setTotalPages(issuePage.getTotalPages());
        fetchIssuesDTO.setLastPage(issuePage.isLast());
        return fetchIssuesDTO;
    }

    public FetchIssueDTO getSingleIssueDetails(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found with id " + issueId));
        return createFetchIssueDTO(issue);
    }
}