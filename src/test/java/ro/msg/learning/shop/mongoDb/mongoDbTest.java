package ro.msg.learning.shop.mongoDb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.msg.learning.shop.repositories.ReportRepository;
import ro.msg.learning.shop.tasks.MonthTask;

@RunWith(SpringRunner.class)
@SpringBootTest
public class mongoDbTest {

    @Autowired
    MonthTask monthTask;

    @Autowired
    ReportRepository reportRepository;

    @Test
    public void testMongo() {

        monthTask.createExcelAndStoreItInMongoDb();

    }

}
