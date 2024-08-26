import { useSnackbar } from 'notistack';

const useApiErrorHandling = () => {
    const { enqueueSnackbar } = useSnackbar();

    const handleApiError = (error, defaultMessage) => {
        if (error.response) {
            const { status, data } = error.response;
            if (status === 400) {
                enqueueSnackbar(data.message || "Bad Request", { variant: "error" });
            } else if (status === 404) {
                enqueueSnackbar("Resource not found", { variant: "error" });
            } else if (status === 500) {
                enqueueSnackbar("Server error, please try again later", { variant: "error" });
            } else {
                enqueueSnackbar(defaultMessage, { variant: "error" });
            }
        } else {
            enqueueSnackbar("Network error, please check your connection", { variant: "error" });
        }
    };

    return { handleApiError };
};

export default useApiErrorHandling;
