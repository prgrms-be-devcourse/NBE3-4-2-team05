// @ts-nocheck
import { useEffect } from "react";
import { Project } from "../constants/project";
import { Decode } from "./useDecode";

const useTokenCheck = () => {
  useEffect(() => {
    const checkInterval = 1000 * 60;

    const interval = setInterval(() => {
      const USER_TOKEN = Project.getJwt();

      if (USER_TOKEN) {
        const decoded = Decode.jwt(USER_TOKEN);

        if (decoded) {
          const exp = decoded?.exp * 1000;
          const now = Date.now();
          const diff = (exp - now) / 60000;

          if (diff < 30) {
            tokenRefresh();
          }
        }
      }
    }, checkInterval);
    return () => clearInterval(interval); // unmount 시 interval 정리
  }, []);

  const tokenRefresh = () => {
    console.log("Token is about to expire. Refreshing...");
  };
};

export default useTokenCheck;
