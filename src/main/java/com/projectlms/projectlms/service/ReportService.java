package com.projectlms.projectlms.service;

import org.springframework.stereotype.Service;
import com.projectlms.projectlms.domain.dao.EnrolledCourse;
import com.projectlms.projectlms.domain.dao.Material;
import com.projectlms.projectlms.domain.dao.Report;
import com.projectlms.projectlms.domain.dao.Section;
import com.projectlms.projectlms.repository.ReportRepository;
import com.projectlms.projectlms.repository.SectionRepository;
import java.util.List;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;
    private final SectionRepository sectionRepository;

    // @Autowired
    // public ReportService(ReportRepository reportRepository, SectionRepository sectionRepository) {
    //     this.reportRepository = reportRepository;
    //     this.sectionRepository = sectionRepository;
    // }

    public void addReport(EnrolledCourse request) {
        try {
            log.info("Add report: {}", request.getCourse().getId());
            List<Section> sections = sectionRepository.searchAllSections(request.getCourse().getId());

            log.info("Get material: ");
            sections.forEach(getMaterial -> {
                List<Material> materials = getMaterial.getMaterials();
                materials.forEach(inputMaterial -> {
                    Report report = new Report();
                    report.setEnrolledCourse(request);
                    report.setMaterial(inputMaterial);
                    reportRepository.save(report);
                });
            });
        } catch (Exception e) {
            log.error("Get an error by executing create new report, Error : {}",e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
