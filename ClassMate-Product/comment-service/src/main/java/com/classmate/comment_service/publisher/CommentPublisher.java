package com.classmate.comment_service.publisher;

import com.classmate.comment_service.dto.CommentDeletionDTO;
import com.classmate.comment_service.dto.comment_count_event.CommentCountEvent;
import com.classmate.comment_service.dto.filedtos.FileDeletionDTO;
import com.classmate.comment_service.dto.notifications.CommentNotificationEventDTO;
import com.classmate.comment_service.dto.notifications.GetForumIdNotificationDTORequest;
import com.classmate.comment_service.dto.notifications.MilestoneReachedEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.file-exchange.name}")
    private String exchange;

    @Value("${rabbitmq.delete-file.routing-key}")
    private String deleteFileRoutingKey;

    @Value("${rabbitmq.delete-comment.routing-key}")
    private String deleteCommentRoutingKey;

    // COMMENT NOTIFICATIONS
    @Value("${rabbitmq.exchange.notifications}")
    private String notificationsExchange;
    @Value("${rabbitmq.notifications-comment.routing-key}")
    private String commentNotificationRoutingKey;

    // MILESTONE NOTIFICATIONS
    @Value("${rabbitmq.notifications.milestone.routing-key}")
    private String milestoneNotificationRoutingKey;

    // GET FORUM ID NOTIFICATIONS
    @Value("${rabbitmq.file-exchange.get.forum.id}")
    private String getForumIdExchange;
    @Value("${rabbitmq.notifications.get.forum.id.routing-key}")
    private String getForumIdNotificationRoutingKey;

    // Comment Count Event
    //QUEUE
    @Value("${rabbitmq.queue.comment-count-event-queue}")
    private String commentCountEventQueue;
    //EXCHANGE
    @Value("${rabbitmq.exchange.comment-count-event}")
    private String commentCountEventExchange;
    // Routing Key
    @Value("${rabbitmq.comment-count-event.routing-key}")
    private String commentCountEventRoutingKey;

    public CommentPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishFileDeleteEvent(FileDeletionDTO event) {
        rabbitTemplate.convertAndSend(exchange, deleteFileRoutingKey, event);
    }

    public void publishCommentDeleteEvent(CommentDeletionDTO event) {
        rabbitTemplate.convertAndSend(exchange, deleteCommentRoutingKey, event);
    }

    // COMMENT NOTIFICATIONS
    public void publishCommentNotificationEvent(CommentNotificationEventDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, commentNotificationRoutingKey, event);
    }

    // VALORATIONS
    public void publishMilestoneReachedEvent(MilestoneReachedEventDTO event) {
        rabbitTemplate.convertAndSend(notificationsExchange, milestoneNotificationRoutingKey, event);
    }

    // GET FORUM ID NOTIFICATIONS
    public void publishGetForumIdNotificationEvent(GetForumIdNotificationDTORequest event) {
        rabbitTemplate.convertAndSend(getForumIdExchange, getForumIdNotificationRoutingKey, event);
    }

    // COMMENT COUNT EVENT
    public void publishCommentCountEvent(Long commentCount, Long postId){
        CommentCountEvent commentCountEvent = CommentCountEvent.builder()
                .commentCount(commentCount)
                .postId(postId)
                .build();
        System.out.println(commentCount);
        System.out.println(postId);
        rabbitTemplate.convertAndSend(commentCountEventExchange, commentCountEventRoutingKey, commentCountEvent);
    }
}
