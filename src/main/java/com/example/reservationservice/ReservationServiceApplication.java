package com.example.reservationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;

@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

}

@RestController
class ReservationRestController {

    Logger logger = LoggerFactory.getLogger(ReservationRestController.class);

    private final ReservationRepository reservationRepository;

    ReservationRestController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    Collection<Reservation> reservationCollections() {
        logger.debug("matt was here!");
        return this.reservationRepository.findAll();
    }
}

@Component
class DataInitializer implements ApplicationRunner {

    private final ReservationRepository reservationRepository;

    DataInitializer(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Stream.of("Josh", "Matt", "Mike", "Jordan",
                "Lucia", "Jen", "Behar", "Rich")
                .forEach(name -> reservationRepository.save(new Reservation(null, name)));

        reservationRepository.findAll().forEach(System.out::println);
    }
}

interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    private String reservationName; // reservation_name
}

@Component
class MyCustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.status("I <3 Production!").build();
    }
}