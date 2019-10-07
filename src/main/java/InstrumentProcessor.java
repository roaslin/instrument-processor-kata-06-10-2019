public class InstrumentProcessor {
    private final TaskDispacher taskDispacher;
    private final Intrument instrument;

    public InstrumentProcessor(TaskDispacher taskDispacher, Intrument instrument) {
        this.taskDispacher = taskDispacher;
        this.instrument = instrument;
    }

    public void process() throws Exception {
        String task = taskDispacher.getTask();

        try {
            instrument.execute(task);
        } catch (FinishedTaskEventException ex) {
            taskDispacher.finishedTask(task);
        } catch (ErrorEventException ex) {
            taskDispacher.error(task);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }
}
