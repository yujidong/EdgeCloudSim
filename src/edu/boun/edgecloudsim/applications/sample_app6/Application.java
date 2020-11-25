package edu.boun.edgecloudsim.applications.sample_app6;

public class Application {
    double usage_percentage;
    double prob_cloud_selection;
    double poisson_interarrival;
    double active_period;
    double idle_period;
    double data_upload;
    double data_download;
    double task_length;
    double required_core;
    double vm_utilization_on_edge;
    double vm_utilization_on_cloud;
    double vm_utilization_on_mobile;
    double delay_sensitivity;

    public Application(double usage_percentage, double prob_cloud_selection, double poisson_interarrival,
                       double active_period, double idle_period, double data_upload, double data_download,
                       double task_length, double required_core, double vm_utilization_on_edge,
                       double vm_utilization_on_cloud, double vm_utilization_on_mobile, double delay_sensitivity) {
        this.usage_percentage = usage_percentage;
        this.prob_cloud_selection = prob_cloud_selection;
        this.poisson_interarrival = poisson_interarrival;
        this.active_period = active_period;
        this.idle_period = idle_period;
        this.data_upload = data_upload;
        this.data_download = data_download;
        this.task_length = task_length;
        this.required_core = required_core;
        this.vm_utilization_on_edge = vm_utilization_on_edge;
        this.vm_utilization_on_cloud = vm_utilization_on_cloud;
        this.vm_utilization_on_mobile = vm_utilization_on_mobile;
        this.delay_sensitivity = delay_sensitivity;
    }

    public double getUsage_percentage() {
        return usage_percentage;
    }

    public double getProb_cloud_selection() {
        return prob_cloud_selection;
    }

    public double getPoisson_interarrival() {
        return poisson_interarrival;
    }

    public double getActive_period() {
        return active_period;
    }

    public double getIdle_period() {
        return idle_period;
    }

    public double getData_upload() {
        return data_upload;
    }

    public double getData_download() {
        return data_download;
    }

    public double getTask_length() {
        return task_length;
    }

    public double getRequired_core() {
        return required_core;
    }

    public double getVm_utilization_on_edge() {
        return vm_utilization_on_edge;
    }

    public double getVm_utilization_on_cloud() {
        return vm_utilization_on_cloud;
    }

    public double getVm_utilization_on_mobile() {
        return vm_utilization_on_mobile;
    }

    public double getDelay_sensitivity() {
        return delay_sensitivity;
    }
}
