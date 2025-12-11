package com.parent.mess.scheduler;

import com.parent.mess.service.MessMenuService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MessMenuCleanupScheduler {

    private final MessMenuService service;

    public MessMenuCleanupScheduler(MessMenuService service) {
        this.service = service;
    }

    // Runs daily at 02:10 AM server time — deletes menus older than today
    @Scheduled(cron = "0 10 2 * * *")
    public void cleanupOldMenus() {
        service.deleteMenusBefore(LocalDate.now());
    }
}
