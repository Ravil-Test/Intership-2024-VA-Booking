package ru.irlix.booking.service;

/**
 * Сервисный слой отправки email
 */
public interface EmailSenderService {

    /**
     * Отправка email
     *
     * @param to      - кому отправлять
     * @param subject - заголовок письма
     * @param body    - текст самого письма
     */
    void sendEmail(String to, String subject, String body);
}
