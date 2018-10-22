package ro.msg.learning.shop.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.msg.learning.shop.entities.Report;

public interface ReportRepository extends MongoRepository<Report, String> {
    Report findByMonthAndYear(Integer month, Integer year);
}
