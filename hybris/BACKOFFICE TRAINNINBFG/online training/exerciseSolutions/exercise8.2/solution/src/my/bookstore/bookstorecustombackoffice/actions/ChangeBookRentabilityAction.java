/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package my.bookstore.bookstorecustombackoffice.actions;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;

import my.bookstore.core.model.BookModel;


public class ChangeBookRentabilityAction implements CockpitAction<BookModel, BookModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(ChangeBookRentabilityAction.class);

	@Resource
	private ModelService modelService;

	@Resource
	private CockpitGlobalEventPublisher cockpitGlobalEventPublisher;

	@Override
	public ActionResult<BookModel> perform(final ActionContext<BookModel> context)
	{
		LOG.info("Entering  ChangeBookRentabilityAction.perform");

		// TODO exercise 8.21: retrieve the reference to the 'currentBook' attribute from the Book Details widget context, which
		// was bound to this Action -- it simply becomes our ActionContext's attached data. Our method's parameter gives us access
		// to this ActionContext. Let's name the local variable currentBook to remind us that it comes from the binding.
		final BookModel currentBook = context.getData();

		// TODO exercise 8.22: check for the value of the book's rentability status using BookModel's getRentable().
		// If it's null, leave it alone. Otherwise, invert the value from TRUE to FALSE and vice versa.
		// Don't forget to persist/save the updated BookModel (you'll need to catch a ModelSavingException)!
		// If the save was successful, publish a Global Cockpit Event that will ultimately trigger refreshing of the widget.
		//
		final Boolean rentableInitialValue = currentBook.getRentable();
		if (rentableInitialValue != null)
		{
			currentBook.setRentable(Boolean.valueOf(!(rentableInitialValue.booleanValue())));
			try
			{
				modelService.save(currentBook);
			}
			catch (final ModelSavingException ex)
			{
				LOG.info("Error persisting BookModel " + currentBook.getName() + " " + currentBook.getCatalogVersion().getVersion());

				// TODO exercise 8.24b: return a new ActionResult<BookModel> object.
				// Make constructor's first parameter ActionResult.ERROR because we caught a ModelSavingException,
				// and make its second parameter be the current book
				return new ActionResult<BookModel>(ActionResult.ERROR, currentBook);
			}

			// TODO exercise 8.23: If a change to a BookModel occurred and the save was successful,
			// publish a Global Cockpit Event to the rest of Backoffice to give notice that this specific book
			// got UPDATED (have this Action instance obtain an injected reference to the CockpitGlobalEventPublisher Spring ben first).
			// Hint: The third parameter of publish() is usually an instance of com.hybris.cockpitng.dataaccess.context.impl.DefaultContext,
			// but use 'null' since we have no data to attach to it, anyway.
			cockpitGlobalEventPublisher.publish(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, currentBook, null);
		}

		// TODO exercise 8.24a: return a new ActionResult<BookModel> object.
		// Make constructor's first parameter ActionResult.SUCCESS, and make its second parameter be the current book
		return new ActionResult<BookModel>(ActionResult.SUCCESS, currentBook);
	}

	@Override
	public boolean canPerform(final ActionContext<BookModel> ctx)
	{
		Validate.notNull("Context may not be null", ctx);

		// We already know from the Java Generic what the return type of getData() will be, and from the Validate that it will be non-null
		// the instanceof is mainly for readability AND I can only assume the JVM will optimize this for us.

		// TODO exercise 8.20: Return true only if the context's getData() method returns a non-null BookModel instance.
		//		(Hint: The <BookModel> Java Generic already forces ctx.getData()'s return type to be BookModel.)
		return ctx.getData() != null;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<BookModel> ctx)
	{
		LOG.info("Entering  ChangeBookRentabilityAction.needsConfirmation");
		return true;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<BookModel> ctx)
	{
		LOG.info("Entering ChangeBookRentabilityAction.getConfirmationMessage");
		Validate.notNull("Context may not be null", ctx);

		final BookModel book = ctx.getData(); // Instantiate from the context
		final ArrayList<String> authors = new ArrayList<String>();

		if (book != null)
		{
			for (final UserModel author : book.getAuthors())
			{
				authors.add(author.getName());
			}

			if (!book.getRentable().booleanValue())
			{
				return ctx.getLabel("make.book.rentable", new Object[]
				{ book.getName(), authors });
			}
			else
			{
				return ctx.getLabel("make.book.unrentable", new Object[]
				{ book.getName(), authors });
			}
		}
		else
		{
			return ctx.getLabel("book.data.insufficient");
		}
	}
}
