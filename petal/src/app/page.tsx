import {redirect} from "next/navigation";

/**
 * Configures the base redirect for the empty url
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Page() {
  redirect('/home')
}