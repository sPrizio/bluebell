import { redirect } from "next/navigation";

/**
 * The default homepage
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function Home() {
  redirect("/dashboard");
}
