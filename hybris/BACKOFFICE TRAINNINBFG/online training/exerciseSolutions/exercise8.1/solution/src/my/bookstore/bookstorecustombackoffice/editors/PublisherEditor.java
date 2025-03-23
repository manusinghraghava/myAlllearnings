package my.bookstore.bookstorecustombackoffice.editors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.impl.InputElement;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractTextBasedEditorRenderer;

import my.bookstore.core.model.BookModel;


// TODO exercise: 8.5: NOTE that the <T> parameter is the java.lang.String Java type
// to correspond to the Editor's java.lang.String Commerce atomic type
public class PublisherEditor extends AbstractTextBasedEditorRenderer<String>
{
	public static final String PARAM_NAME_CHAR_LIMIT = "charLimit";
	public static final String PARAM_NAME_NO_DIGITS = "enforceNoDigits";

	private boolean noDigitsIsEnforced = false; // design default
	private int charLimit = 0; // the default of 0 indicates char limit not enforced

	private static final Logger LOG = LoggerFactory.getLogger(PublisherEditor.class);
//	protected BookModel parentObject;
	protected EditorContext<String> editorCtx;

	protected Vlayout publisherEditorVlayout;
	protected Hlayout charLimitRow;
	protected Label noDigitsAllowedLabel;
	protected Label charsValueLabel;
	protected Label charLimitValueLabel;
	protected Label charsLeftValueLabel;
	public static final String LABEL_HEADER_CHARS = "Chars:";
	public static final String LABEL_HEADER_CHAR_LIMIT = "Limit:";
	public static final String LABEL_HEADER_CHARS_LEFT = "Chars left:";
	public static final String STYLE_RED = "color: red";
	public static final String STYLE_RED_BOLD = STYLE_RED + "; " + "font-weight: bold";
	public static final String STYLE_RED_UNDERLINE = STYLE_RED + "; " + "text-decoration: underline";

	@Override
	protected void setRawValue(final InputElement inputElement, final String s)
	{
		LOG.info("Entering PublisherEditor.setRawValue");
		inputElement.setRawValue(s);
	}

	@Override
	protected String getRawValue(final InputElement inputElement)
	{
		LOG.info("Entering PublisherEditor.getRawValue");
		return (String) inputElement.getRawValue();
	}

	@Override
	protected String coerceFromString(final InputElement inputElement, final String s) throws WrongValueException
	{
		LOG.info("Entering PublisherEditor.coerceFromString");
		return s;
	}


	@Override
	public void render(final Component parent, final EditorContext<String> context, final EditorListener<String> listener)
	{
		LOG.info("Entering PublisherEditor.render");
		Validate.notNull("All arguments must be non-null", parent, context, listener);

		// TODO exercise 8.6: Uncomment this line to allow reading-in of parameters from your Editor's view configuraion
		initializeEditorConfigurationParameters(context);


		// render() attribute 'parent' refers to the Editor ZUL component to which we attach our built 'editor view' construct
		// of zul components
		//
		// render() attribute 'context' has an attached 'parentObject' which is the BookModel being edited
		// render() attribute 'context' has an attached 'initialValue' which is the String value of BookModel's 'publisher'


		// To access the "enclosing object" (here, named parentObject) of the attribute your Editor is handling, do this:
		// (I.e., our Editor is handling the 'publisher' attribute of this "parentObject" BookModel instance.)
		//parentObject = (BookModel) context.getParameter("parentObject");


		// TODO exercise 8.7: Uncomment the commented-out block of lines below, then study them to understand what they do

		this.publisherEditorVlayout = new Vlayout();

		final Textbox publisherAttribTextbox = new Textbox();
		publisherAttribTextbox.setId("publisherAttribTextbox");
		this.publisherEditorVlayout.appendChild(publisherAttribTextbox);

		// Place an initial value into the newly instantiated Textbox instance.
		this.editorCtx = context;
		publisherAttribTextbox.setValue(context.getInitialValue());





		// TODO exercise 8.8: Uncomment the large block of lines below, then study them to understand what they do

		this.noDigitsAllowedLabel = new Label("NO DIGITS ALLOWED");
		this.noDigitsAllowedLabel.setVisible(false);
		this.noDigitsAllowedLabel.setStyle(STYLE_RED_BOLD);
		this.publisherEditorVlayout.appendChild(noDigitsAllowedLabel);


		this.charLimitRow = new Hlayout();
		this.charLimitRow.setVisible(this.charLimit > 0); //visible only if char limit is enforced (relies on Editor's ZUL params)
		this.publisherEditorVlayout.appendChild(this.charLimitRow);

		final Label charsHeaderLabel = new Label(LABEL_HEADER_CHARS);
		charsHeaderLabel.setStyle(STYLE_RED);
		this.charLimitRow.appendChild(charsHeaderLabel);

		this.charsValueLabel = new Label();
		this.charsValueLabel.setStyle(STYLE_RED_BOLD);
		this.charsValueLabel.setValue(String.valueOf(chars(publisherAttribTextbox)));
		this.charLimitRow.appendChild(this.charsValueLabel);


		final Label charLimitHeaderLabel = new Label(LABEL_HEADER_CHAR_LIMIT);
		charLimitHeaderLabel.setStyle(STYLE_RED);
		this.charLimitRow.appendChild(charLimitHeaderLabel);

		this.charLimitValueLabel = new Label();
		this.charLimitValueLabel.setStyle(STYLE_RED_BOLD);
		this.charLimitValueLabel.setValue(String.valueOf(this.charLimit));
		this.charLimitRow.appendChild(charLimitValueLabel);


		final Label charsLeftHeaderLabel = new Label(LABEL_HEADER_CHARS_LEFT);
		charsLeftHeaderLabel.setStyle(STYLE_RED);
		this.charLimitRow.appendChild(charsLeftHeaderLabel);

		this.charsLeftValueLabel = new Label();
		this.charsLeftValueLabel.setStyle(STYLE_RED_BOLD);
		this.charsLeftValueLabel.setValue(String.valueOf(charsLeft(publisherAttribTextbox)));
		this.charLimitRow.appendChild(this.charsLeftValueLabel);


		// TODO exercise 8.9a: to setup event forwarding of the ZK component events to your Editor Java code,
		// add (uncomment) a call to the initViewComponent() method inherited from
		// this Editor class' parent class.  It takes three parameters.
		initViewComponent(publisherAttribTextbox, context, listener);


		// TODO exercise 8.9b: Attach the above "view fragment" to this Editor's provided "attachment" Component
		this.publisherEditorVlayout.setParent(parent);
	}

	private void initializeEditorConfigurationParameters(final EditorContext<String> context)
	{
		// initialize Editor parameters from widget's ZUL or *-config.xml

		if (context == null)
		{
			LOG.info("*** EditorContext is null -- cannot read or set params - falling back on default values.");
			return;
		}

		final Object charLimitParamObj = context.getParameter(PARAM_NAME_CHAR_LIMIT);
		LOG.info("*** Editor config param " + PARAM_NAME_CHAR_LIMIT + " came in as '" + charLimitParamObj + "'");
		if (charLimitParamObj instanceof String) //includes check for null
		{
			try
			{
				this.charLimit = Integer.parseInt((String) charLimitParamObj);
				LOG.info(">>> initialized parameter... key: " + PARAM_NAME_CHAR_LIMIT + " to: " + this.charLimit);
			}
			catch (final Exception e)
			{
				LOG.info("*** Editor config param " + PARAM_NAME_CHAR_LIMIT + " value:'" + charLimitParamObj + "' threw " + e
						+ " when trying to parse to an 'int' - falling back on default value '" + this.charLimit + "'.");
			}
		}
		else
		{
			LOG.info("*** Editor config param " + PARAM_NAME_CHAR_LIMIT + " is null or missing - falling back on default value '"
					+ this.charLimit + "'.");
		}



		final Object enforceNoDigitsParamObj = context.getParameter(PARAM_NAME_NO_DIGITS);
		LOG.info("*** parameter " + PARAM_NAME_NO_DIGITS + " came in as '" + enforceNoDigitsParamObj + "'");
		this.noDigitsIsEnforced = context.getParameterAsBoolean(PARAM_NAME_NO_DIGITS, false);
		LOG.info(">>> initialized value of '" + PARAM_NAME_NO_DIGITS + "' to: " + this.noDigitsIsEnforced);
	}



	@Override
	protected void onChangingEvent(final EditorListener<String> listener, final InputElement eventElement, final InputEvent event)
	{
		//NOTE: *** 'InputElement editorView' in this method is NOT what render() attached to 'parent', it's the element that caused the event
		//	     *** ALSO, "onChanging" means "on ABOUT TO change", so the element's getValue() returns the 'before' state of the component (i.e., just before the keystroke that caused the event to fire)
		super.onChangingEvent(listener, eventElement, event);
		LOG.info("*** In onChangingEvent (alt facade method)");
	}


	// TODO 8.10: Notice how these inherited methods can be used as interceptors if they are overridden,
	// then (and this is very, very important) placing a call to the super method at the END of
	// the overriding method (see last line of this method.

	// ZK event that fires if the current value (returned by getRawValue(),
	// (which is NOT necessarily the value being displayed/edited IF a constraint is active)
	//		does NOT EQUAL initially value
	@Override
	protected void onChangeEvent(final EditorListener<String> listener, final InputElement eventElement)
	{
		// eventElement is the element that caused the ZK event to fire (i.e., the Textbox that we bound the ZK events to Backoffice
		LOG.info("*** In onChangeEvent");

		// Block digits first /before/ truncating, thus preserving more of the original value
		if (this.noDigitsIsEnforced && containsDigits(eventElement.getText()))
		{
			eventElement.setText(eventElement.getText().replaceAll("[0-9]", ""));
			this.noDigitsAllowedLabel.setVisible(true);
		}

		// Truncate value to CHAR_LIMIT length
		if (this.charLimit > 0 && charsLeft(eventElement) <= 0)
		{
			eventElement.setText(eventElement.getText().substring(0, this.charLimit));
			// Follow-up calls to parentObject.setPublisher() to propagate data values to be saved
			// are handled by the event handlers/notifiers that were attached as handlers to these ZK events
		}

		/*
		 * setRawValue() sets the display value, bypassing the actual ZK component "state value", thus bypassing validation.
		 * setText() sets the actual value of the ZK component, which is what the validator sees AND sets the displayed valye.
		 */

		this.charLimitValueLabel.setValue(String.valueOf(this.charLimit));
		this.charsValueLabel.setValue(String.valueOf(chars(eventElement)));
		this.charsLeftValueLabel.setValue(String.valueOf(charsLeft(eventElement)));

		// VERY, VERY, VERY IMPORTANT to place this super.onChangeEvent(..) and the END of your overriding method,
		//  otherwise your custom manipulations will affect only the display AFTER the value gets sent to the comerce suite to SAVE
		//  and you'll be VERY, VERY confused
		super.onChangeEvent(listener, eventElement);

	}

	// onChangeEvent() helper method
	private String validPortionOfValue(final InputElement eventElement)
	{
		return (String) eventElement.getRawValue();
	}

	// onChangeEvent() helper method
	private int chars(final InputElement eventElement)
	{
		return validPortionOfValue(eventElement).length();
	}

	// onChangeEvent() helper method
	private int charsLeft(final InputElement eventElement)
	{
		return this.charLimit - chars(eventElement);
	}

	// onChangeEvent() helper method
	private boolean containsDigits(final String s)
	{
		final boolean result = s.matches(".*[0-9].*");
		LOG.info("*** in onChangeEvent.containsDigits(), returning " + result + " for input s:" + s);
		return result;
	}





	// ZK only fires this event if the user hit the ENTER key while in this Textbox AND its submitByEnter attrib is (default)true (else, allows ENTER to be part of the value)
	@Override
	protected void onOkEvent(final EditorListener<String> listener, final InputElement eventElement)
	{
		LOG.info("*** In onOkEvent");
		super.onOkEvent(listener, eventElement);
	}

	// ZK only fires this event if the user hit the ESC key while in this Textbox
	@Override
	protected void onCancelEvent(final EditorListener<String> listener, final InputElement eventElement, final String initialValue)
	{
		LOG.info("*** In onCancelEvent");
		super.onCancelEvent(listener, eventElement, initialValue);
	}


	@Override
	protected void onFocusEvent(final InputElement eventElement, final String initialEditText)
	{
		//		LOG.info("*** In onFocusEvent");
		super.onFocusEvent(eventElement, initialEditText);
	}

}
