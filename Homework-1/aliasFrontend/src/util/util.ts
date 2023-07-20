import {useEffect, useState} from "react";
import axios from "axios";

export function useAxiosGet<T>(url: string, transform: (json: T) => T, payload: any = {}) {
    const [data, setData] = useState<T | null>(null);
    const [error, setError] = useState("");
    const [loaded, setLoaded] = useState(false);

    useEffect(() => {
        axios
            .get(url, payload)
            .then((response) => setData(transform(response.data as T)))
            .catch((error) => setError(error.message))
            .finally(() => setLoaded(true));
    }, []);

    return {data, error, loaded};
}
