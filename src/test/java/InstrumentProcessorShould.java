import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentProcessorShould {

    @Mock
    private TaskDispacher taskDispacher;
    @Mock
    private Intrument instrument;
    @Mock
    private Console console;

    private InstrumentProcessor instrumentProcessor;

    @Before
    public void setUp() {
        this.instrumentProcessor = new InstrumentProcessor(taskDispacher, instrument, console);
    }

    @Test
    public void execute_tasks() throws Exception {
        String task = "taskToExecute";
        given(taskDispacher.getTask()).willReturn(task);
        doThrow(new FinishedTaskEventException()).when(instrument).execute(task);

        instrumentProcessor.process();

        verify(taskDispacher, times(1)).getTask();
        verify(instrument, times(1)).execute(task);
        verify(taskDispacher, times(1)).finishedTask(task);
    }

    @Test(expected = Exception.class)
    public void handle_exceptions() throws Exception {
        given(taskDispacher.getTask()).willReturn(null);
        doThrow(new IllegalArgumentException()).when(instrument).execute(null);

        instrumentProcessor.process();

        verify(taskDispacher, times(1)).getTask();
        verify(instrument, times(1)).execute(null);
    }

    @Test
    public void fire_event_when_task_is_finished() throws Exception {
        String task = "taskToExecute";
        given(taskDispacher.getTask()).willReturn(task);
        doThrow(new FinishedTaskEventException()).when(instrument).execute(task);

        instrumentProcessor.process();

        verify(taskDispacher, times(1)).getTask();
        verify(instrument, times(1)).execute(task);
        verify(taskDispacher, times(1)).finishedTask(task);
    }

    @Test
    public void prints_task_into_the_console_when_theres_an_error() throws Exception {
        String task = "taskToExecute";
        given(taskDispacher.getTask()).willReturn(task);
        doThrow(new ErrorEventException(task)).when(instrument).execute(task);

        instrumentProcessor.process();

        verify(taskDispacher, times(1)).getTask();
        verify(instrument, times(1)).execute(task);
        verify(console, times(1)).print(task);
    }
}
