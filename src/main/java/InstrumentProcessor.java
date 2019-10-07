public class InstrumentProcessor {
    private final TaskDispacher taskDispacher;
    private final Intrument instrument;
    private final Console console;

    public InstrumentProcessor(TaskDispacher taskDispacher, Intrument instrument, Console console) {
        this.taskDispacher = taskDispacher;
        this.instrument = instrument;
        this.console = console;
    }

    public void process() throws Exception {
        String task = taskDispacher.getTask();

        try {
            instrument.execute(task);
        } catch (FinishedTaskEventException ex) {
            taskDispacher.finishedTask(task);
        } catch (ErrorEventException ex) {
            console.print(task);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }
}
