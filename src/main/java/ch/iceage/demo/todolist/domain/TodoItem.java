package ch.iceage.demo.todolist.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 11:55:27
 */
@Entity
@Table(name="TODO_ITEM")
public class TodoItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Column(name="img_url")
	private String ticketImageUrl;
	@Column(name="attached_doc_url")
	private String attachedDocumentUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTicketImageUrl() {
		return ticketImageUrl;
	}

	public void setTicketImageUrl(String ticketImageUrl) {
		this.ticketImageUrl = ticketImageUrl;
	}

	public String getAttachedDocumentUrl() {
		return attachedDocumentUrl;
	}

	public void setAttachedDocumentUrl(String attachedDocumentUrl) {
		this.attachedDocumentUrl = attachedDocumentUrl;
	}
}
